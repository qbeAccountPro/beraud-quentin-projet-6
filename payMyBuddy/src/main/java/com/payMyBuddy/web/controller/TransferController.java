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

import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

import java.util.ArrayList;
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

  @GetMapping("")
  void getTransfer(Model model) {
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

  @RequestMapping("/addConnection")
  @ResponseBody
  public ResponseEntity<String> checkEmail(@RequestParam("email") String email, Model model) {

    boolean emailExists = true; // TODO
    boolean emailIsNotFromUser = true; // TODO
    Boolean emailIsNotFromContact = true; // TODO
    if (!emailExists) {
      System.out.println("1");
      return ResponseEntity.ok("wrongNotExists");
    } else if (!emailIsNotFromUser) {
      System.out.println("12");
      return ResponseEntity.ok("wrongUserEmail");
    } else if (!emailIsNotFromContact) {
      System.out.println("123");
      return ResponseEntity.ok("wrongContactEmail");
    } else {
      System.out.println("1234");
      return ResponseEntity.ok("exists");
    }
  }
}
