package com.paymybuddy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TransferController {

  @Autowired
  UserService userService;

  @Autowired
  ContactService contactService;

  @Autowired
  TransactionService transactionService;

  private List<String> authorizedEmails = Arrays.asList("email1@example.com", "email2@example.com");

  @GetMapping("/transfer")
  void login(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();
      User user = userService.getUserByUsername(username);

      // Get all connection/contact from logged in user
      List<Integer> contactsId = contactService.getAllContactIdForAnUser(user);
      List<User> userContacts = userService.getUsersById(contactsId);
      List<String> contactsMail = userContacts.stream()
          .map(User::getMail)
          .collect(Collectors.toList());
      model.addAttribute("contacts", contactsMail);

      // Get all Transactions from logged in user
      List<Transaction> transactions = new ArrayList<>();
      transactions = transactionService.getAllTransactionForAnUser(user);
      model.addAttribute("transactions", transactions);
    }
  }

  @PostMapping("/addConnection")
  public String addConnection(@RequestParam("modal-email") String email, RedirectAttributes redirectAttributes) {

    if (authorizedEmails.contains(email)) {
      System.out.println("Contact ajouté");
      return "redirect:/1";
    } else {
      redirectAttributes.addAttribute("error", true);
      System.out.println("Contact introuvable");
      return "redirect:/2";
    }
  }
}
