package com.paymybuddy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.web.model.Contact;
import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transfer")
public class TransferController {

  @Autowired
  UserService userService;

  @Autowired
  ContactService contactService;

  @Autowired
  TransactionService transactionService;

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String mail = userDetails.getUsername();
      return userService.getUserByMail(mail);
    } else
      return null;

  }

  @GetMapping("")
  void loadTranfer(Model model) { // TODO AFFICHER LE SOLDE
    User user = getCurrentUser();
    model.addAttribute("contacts", getContactMails(user));
    model.addAttribute("transactions", transactionService.getAllTransactionForAnUser(user));
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
        BigDecimal applicationMonetization = new BigDecimal(0.05);
        BigDecimal amount = new BigDecimal(amountString);
        BigDecimal finalAmount = amount.add(amount.multiply(applicationMonetization));
        finalAmount = finalAmount.setScale(2, RoundingMode.HALF_UP);
        bankBalance = bankBalance.subtract(finalAmount);

        if (bankBalance.compareTo(BigDecimal.ZERO) < 0) {
          // TODO AJOUTER AU MOINS LE DETAIL DE PK LE MONTANT EST INNSUFISANT dans la vue
          return ResponseEntity.ok("bankBalanceInsufficient");
        } else {
          Transaction transaction = new Transaction();
          transaction.setDate(date);
          transaction.setDebitUserId(currentUser.getId());
          transaction.setCreditUserId(creditUser.getId());
          transaction.setDescription(description);
          transaction.setFare(amount);

          // TODO VOIR SI ERREURE OU CRASH COMMENT ROLL BACK
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
