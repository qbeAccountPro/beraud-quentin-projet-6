package com.paymybuddy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.web.communUtilts.DataManipulationUtils;
import com.paymybuddy.web.dto.ProfileDto;
import com.paymybuddy.web.logging.EndpointsLogger;
import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.BankAccountService;
import com.paymybuddy.web.service.ProfileService;
import com.paymybuddy.web.service.UserService;

import jakarta.transaction.Transactional;

/**
 * Some javadoc :
 * 
 * This class represent the controller of profile bank.
 */
@Controller
@RequestMapping("/profileBank")
public class ProfileBankController {

  @Autowired
  UserService userService;

  @Autowired
  BankAccountService bankAccountService;

  @Autowired
  ProfileService profileBankService;

  EndpointsLogger log = new EndpointsLogger();

  /**
   * Some javadoc :
   * 
   * This method represent the mapping of profileBank.
   * It will get the information on user like a bank information and balance.
   * 
   * @param model hold information from or for HTML page.
   * 
   * @return the profile bank page.
   */
  @GetMapping("")
  String getProfile(Model model) {
    User user = userService.getCurrentUser();
    BankAccount bankAccount = bankAccountService.findBankAccountByUser(user);
    ProfileDto profile = profileBankService.getProfile(user, bankAccount);
    model.addAttribute("profilePage", profile);
    return "profileBank";
  }

  /**
   * Some javadoc :
   * 
   * This method represent the mapping of creditAccount.
   * It will credit the currently authenticated user.
   * 
   * @param amountPBM represent the amount to credit on PayMyBuddy account.
   * @param model     hold information from or for HTML page.
   * 
   * @return responseEntity contain the message to confirm or not the
   *         transaction.
   */
  @ResponseBody
  @Transactional
  @PostMapping("/creditAccount")
  public ResponseEntity<String> creditAccount(@RequestParam("amountPMB") String amountPMB, Model model) {
    String methodeName = DataManipulationUtils.getCurrentMethodName();
    User user = userService.getCurrentUser();
    log.request(methodeName, amountPMB, user);
    return userService.creditUserAccount(user, amountPMB, methodeName);
  }

  /**
   * Some javadoc :
   * 
   * This method represent the mapping of debitAccount.
   * It will debit the currently authenticated user
   * 
   * @param amountBank represent the amount to debit from PayMyBuddy account to
   *                   Bank.
   * @param model      hold information from or for HTML page.
   * 
   * @return responseEntity contain the message to confirm or not the
   *         transaction.
   */
  @ResponseBody
  @Transactional
  @PostMapping("/debitAccount")
  public ResponseEntity<String> debitAccount(@RequestParam("amountBank") String amountBank, Model model) {
    String methodeName = DataManipulationUtils.getCurrentMethodName();
    User user = userService.getCurrentUser();
    log.request(methodeName, amountBank, user);
    return userService.debitUserAccount(user, amountBank, methodeName);
  }
}
