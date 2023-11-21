package com.paymybuddy.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.web.dto.MyTransactionDto;
import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.Contact;
import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.BankAccountService;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/transfer")
public class TransferController {

  @Autowired
  UserService userService;

  @Autowired
  ContactService contactService;

  @Autowired
  TransactionService transactionService;

  @Autowired
  BankAccountService bankAccountService;

  public User getCurrentUser() { // TODO : a dédoubler car présent dans profile bank
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String mail = userDetails.getUsername();
      return userService.getUserByMail(mail);
    } else if (authentication.getPrincipal() instanceof OAuth2User) {
      OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
      String email = oauth2User.getAttribute("email");
      if (email != null) {
        User user = userService.getUserByMail(email);
        if (user != null) {
          return user;
        } else {
          user = new User();
          user.setMail(email);
          user.setBankBalance(new BigDecimal(0));
          user.setFirstName(oauth2User.getAttribute("given_name"));
          user.setLastName(oauth2User.getAttribute("family_name"));
          User userCreated = userService.addUser(user);
          BankAccount bankAccount = new BankAccount();
          bankAccount.setUserId(userCreated.getId());
          bankAccount.setBankName("Create formular to update new user");
          bankAccount.setIBAN(000);
          bankAccountService.addBankAccount(bankAccount);
          return user;
          // TODO : creat a page to inscription
        }
      } else {
        // TODO : Gérer le cas où l'adresse e-mail n'est pas disponible dans les
        // attributs OAuth2
        return null;
      }
    } else {
      return null; // TODO géré l'erreure
    }
  }

  @GetMapping("")
  void loadTranfer(Model model) {
    User user = getCurrentUser();
    if (user != null) {
      model.addAttribute("contacts", getContactMails(user));
      List<MyTransactionDto> allTransactionDto = transactionService.getTransactionsDto(user);
      model.addAttribute("transactions", allTransactionDto);
      model.addAttribute("balance", user.getBankBalance());
    } else {
      // TODO géré le cas ou l'utilisateur ne peux etre créer
    }
  }

  @ResponseBody
  @PostMapping("/addConnection")
  public ResponseEntity<String> checkEmail(@RequestParam("mail") String mail, Model model) {
    if (mail.isEmpty()) {
      return ResponseEntity.ok("mail-empty");
    }
    User contactUser = userService.getUserByMail(mail);
    if (contactUser == null) {
      return ResponseEntity.ok("mail-nonexistent");
    }
    User currentUser = getCurrentUser();
    String mailCurrentUser = currentUser.getMail();
    if (mailCurrentUser.equals(mail)) {
      return ResponseEntity.ok("mail-FromCurrentUser");
    }
    List<Integer> userContacts = contactService.getAllContactIdForAnUser(currentUser);
    Boolean emailFromContact = userContacts.contains(contactUser.getId());

    if (emailFromContact) {
      return ResponseEntity.ok("mail-FromContactList");
    } else {
      Contact newContact = new Contact();
      newContact.setUser1Id(currentUser.getId());
      newContact.setUser2Id(contactUser.getId());
      contactService.addContact(newContact);

      model.addAttribute("contacts", getContactMails(currentUser));
      return ResponseEntity.ok("mail-Added");
    }
  }

  List<String> getContactMails(User user) {
    List<User> userContacts = userService.getListUserById(contactService.getAllContactIdForAnUser(user));
    return userContacts.stream()
        .map(User::getMail)
        .collect(Collectors.toList());
  }

  @ResponseBody
  @Transactional
  @RequestMapping("/pay")
  public ResponseEntity<String> payUrBuddy(@RequestParam("date") String dateString,
      @RequestParam("amount") String amountString,
      @RequestParam("connection") String connection, @RequestParam("description") String description, Model model)
      throws ParseException {
    try {
      if (description.isEmpty()) {
        return ResponseEntity.ok("emptyDescription");
      } else {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateString);

        User currentUser = getCurrentUser();
        User creditUser = userService.getUserByMail(connection);
        BigDecimal bankBalance = currentUser.getBankBalance();
        BigDecimal applicationMonetization = new BigDecimal(0.005);// TODO create constant
        BigDecimal amount = new BigDecimal(amountString);
        BigDecimal monetizedFare = amount.multiply(applicationMonetization);
        monetizedFare = monetizedFare.setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalAmount = amount.add(monetizedFare);
        bankBalance = bankBalance.subtract(finalAmount);

        if (bankBalance.compareTo(BigDecimal.ZERO) < 0) {
          return ResponseEntity.ok("bankBalanceInsufficient");
        } else {
          Transaction transaction = new Transaction();
          transaction.setDate(date);
          transaction.setDebitUserId(currentUser.getId());
          transaction.setCreditUserId(creditUser.getId());
          transaction.setDescription(description);
          transaction.setFare(amount);
          transaction.setMonetizedFare(monetizedFare);
          userService.makeTransaction(transaction);
          transactionService.addTransaction(transaction);
          return ResponseEntity.ok("payDone");
        }
      }
    } catch (Exception e) {
      return ResponseEntity.ok("errorException");
    }
  }
}
