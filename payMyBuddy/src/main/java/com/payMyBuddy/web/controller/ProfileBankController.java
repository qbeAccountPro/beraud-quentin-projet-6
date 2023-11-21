package com.paymybuddy.web.controller;

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

import com.paymybuddy.web.dto.ProfilePage;
import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.BankAccountService;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.ProfileBankService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/profileBank")
public class ProfileBankController {

  @Autowired
  UserService userService;

  @Autowired
  ContactService contactService;

  @Autowired
  BankAccountService bankAccountService;

  @Autowired
  TransactionService transactionService;

  @Autowired
  ProfileBankService profileBankService;

  @GetMapping("")
  String getProfile(Model model) {
    User user = getCurrentUser();
    BankAccount bankAccount = bankAccountService.findBankAccountByUser(user);
    ProfilePage profilePage = profileBankService.getProfilePage(user, bankAccount);
    model.addAttribute("profilePage", profilePage);
    return "profileBank";
  }

  User getCurrentUser() { // TODO : a dédoubler car présent dans profile bank
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
        return user;
      }
      return null;
    } else {
      // TODO : Gérer le cas où l'adresse e-mail n'est pas disponible dans les
      // attributs OAuth2
      return null;
    }
  }

  @ResponseBody
  @Transactional
  @PostMapping("/creditAccount")
  public ResponseEntity<String> creditAccount(@RequestParam("amountPMB") String amountPMB, Model model) {
    User user = getCurrentUser();
    return userService.creditUserAccount(user, amountPMB);// TODO
  }

  @ResponseBody
  @Transactional
  @PostMapping("/debitAccount")
  public ResponseEntity<String> debitAccount(@RequestParam("amountBank") String amountBank, Model model) {
    User user = getCurrentUser();
    return userService.debitUserAccount(user, amountBank);// TODO
  }
}
