package com.paymybuddy.web.controller;

import java.text.ParseException;
import java.util.List;

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
import com.paymybuddy.web.dto.MyTransactionDto;
import com.paymybuddy.web.logging.EndpointsLogger;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.BankAccountService;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

import jakarta.transaction.Transactional;

/**
 * Some javadoc :
 * 
 * This class represent the controller of transfer page.
 */
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

  EndpointsLogger log = new EndpointsLogger();

  /**
   * Some javadoc :
   * 
   * This method represent the mapping of transfer.
   * It will load and set up all information from the currently authenticated user
   * on Transfer page.
   * 
   * @param model hold information for transfer page.
   */
  @GetMapping("")
  String loadTranfer(Model model) {
    User user = userService.getCurrentUser();
    if (user != null) {
      List<MyTransactionDto> allTransactionDto = transactionService.getTransactionsDto(user);
      model.addAttribute("contacts", contactService.getContactMails(user));
      model.addAttribute("transactions", allTransactionDto);
      model.addAttribute("balance", user.getBankBalance());
      return "transfer";
    } else {
      return "error";
    }
  }

  /**
   * Some javadoc :
   * 
   * This method represent the addConnection mapping.
   * It will check if the new connection doesn't exist.
   * If the mail exist into the database.
   * 
   * @param mail  of the new connection.
   * @param model hold information from or for HTML page.
   * @return responseEntity contain the message to confirm or not the
   *         added connection.
   */
  @ResponseBody
  @PostMapping("/addConnection")
  public ResponseEntity<String> addConnection(@RequestParam("mail") String mail, Model model) {
    String methodeName = DataManipulationUtils.getCurrentMethodName();
    log.request(methodeName, mail);
    return userService.checkEmail(mail, model, methodeName);
  }

  /**
   * Some javadoc :
   * 
   * This method represent the pay mapping.
   * It will check and send a transfer to a specific user from the currently
   * authentication user.
   * 
   * @param date        the current date.
   * @param amount      the amount of transfer.
   * @param connection  of credited user.
   * @param description of the transfer.
   * @param model       hold information from or for HTML page.
   * @return responseEntity contain the message to confirm or not the
   *         payment.
   */
  @ResponseBody
  @Transactional
  @RequestMapping("/pay")
  public ResponseEntity<String> makeTransaction(@RequestParam("date") String date,
      @RequestParam("amount") String amount,
      @RequestParam("connection") String connection, @RequestParam("description") String description, Model model)
      throws ParseException {
    String methodeName = DataManipulationUtils.getCurrentMethodName();
    log.request(methodeName, date, amount, connection, description);
    return transactionService.makeTransaction(date, amount, connection, description, model);
  }
}
