package com.paymybuddy.web.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.paymybuddy.web.communUtilts.DataManipulationUtils;
import com.paymybuddy.web.constants.Constants;
import com.paymybuddy.web.dto.MyTransactionDto;
import com.paymybuddy.web.logging.EndpointsLogger;
import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.repository.TransactionRepository;

/**
 * Some javadoc.
 * 
 * Service class responsible for Transaction entities.
 * This class contains business logic and serves as an intermediary between
 * controllers and repositories.
 */
@Service
public class TransactionService {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  UserService userService;

  @Autowired
  ContactService contactService;

  EndpointsLogger log = new EndpointsLogger();

  /**
   * Some javadoc.
   * 
   * @return a collection of transactions.
   */
  public Iterable<Transaction> getTransactions() {
    return transactionRepository.findAll();
  }

  /**
   * Some javadoc.
   * 
   * Retrieves and returns a Transaction by id.
   * 
   * @param id : id to retrieves.
   * 
   * @return an optional Transaction.
   */
  public Optional<Transaction> getTransactionById(Integer id) {
    return transactionRepository.findById(id);
  }

  /**
   * Some javadoc.
   * 
   * Save a Transaction object.
   * 
   * @param transaction : Transaction object to save.
   * 
   * @return a Transaction.
   */
  public Transaction addTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  /**
   * Some javadoc.
   * 
   * Delete a Transaction object by id.
   * 
   * @param transaction : Transaction object to delete.
   */
  public void deleteTransaction(Transaction transaction) {
    transactionRepository.deleteById(transaction.getId());
  }

  /**
   * Some javadoc.
   * 
   * Update a Transaction object.
   * 
   * @param transaction : Transaction object to update.
   */
  public void updateTransaction(Transaction transaction) {
    Optional<Transaction> foundTransaction = transactionRepository.findById(transaction.getId());
    if (foundTransaction.isPresent()) {
      addTransaction(transaction);
    }
  }

  /**
   * Some javadoc.
   * 
   * Get transactionsDto from a user.
   * 
   * @param user : user object.
   */
  public List<MyTransactionDto> getTransactionsDto(User user) {
    List<Transaction> transactions = transactionRepository.findTransactionByUserId(user.getId());
    Collections.sort(transactions, Comparator.comparing(Transaction::getDate).reversed());
    return convertTransactionsIntoDto(transactions, user);
  }

  /**
   * Some javadoc :
   * 
   * Convert Transactions entities into MyTransactionDto entities.
   * 
   * @Param transactions it's a litst of transactions
   * @Param user : equivalent of "user" entity.
   */
  public List<MyTransactionDto> convertTransactionsIntoDto(List<Transaction> transactions, User user) {
    List<MyTransactionDto> transactionsDto = new ArrayList<>();
    for (Transaction transaction : transactions) {
      int currentUserId = user.getId();
      MyTransactionDto newTransactionDto = new MyTransactionDto();
      if (currentUserId == transaction.getCreditUserId()) {
        newTransactionDto = getTransactionDataForDto(transaction, true);
        User debitUser = userService.getUserById(transaction.getDebitUserId());
        newTransactionDto.setFirstName(debitUser.getFirstName());
        newTransactionDto.setLastName(debitUser.getLastName());
      } else {
        newTransactionDto = getTransactionDataForDto(transaction, false);
        User creditUser = userService.getUserById(transaction.getCreditUserId());
        newTransactionDto.setFirstName(creditUser.getFirstName());
        newTransactionDto.setLastName(creditUser.getLastName());
      }
      transactionsDto.add(newTransactionDto);
    }
    return transactionsDto;
  }

  /**
   * Some javadoc :
   * 
   * Convert transaction entity into a data transfer object : "MyTransactionDto".
   * 
   * @param transaction represent the transaction entity to be converted.
   * @param creditSafe  represents a boolean to validate the rate to be added or
   *                    not to the DTO.
   * @retun the entity "MyTransactionDto".
   */
  private MyTransactionDto getTransactionDataForDto(Transaction transaction, boolean creditFare) {
    MyTransactionDto myTransactionDto = new MyTransactionDto();
    myTransactionDto.setDescription(transaction.getDescription());
    myTransactionDto.setDate(transaction.getDate());
    if (creditFare) {
      myTransactionDto.setFare(transaction.getFare());
    } else {
      myTransactionDto.setFare(transaction.getFare().negate());
      myTransactionDto.setMonetizedFare(transaction.getMonetizedFare().negate());
    }
    return myTransactionDto;
  }

  /**
   * Some javadoc :
   * 
   * This method represent the pay service.
   * It will check and send a transfer to a specific user from the currently
   * authentication user.
   * 
   * @param dateString   the current date.
   * @param amountString the amount of transfer.
   * @param connection   of credited user.
   * @param description  of the transfer.
   * @param model        hold information from or for HTML page.
   * @return responseEntity contain the message to confirm or not the
   *         payment.
   */
  public ResponseEntity<String> makeTransaction(String dateString, String amountString, String connection,
      String description, Model model) {
    try {
      String methodName = DataManipulationUtils.getCurrentMethodName();
      if (description.isEmpty()) {
        return log.emptyDescription(methodName);
      } else {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateString);
        User user = userService.getCurrentUser();
        User contact = userService.getUserByMail(connection);
        BigDecimal balance = user.getBankBalance();
        BigDecimal amount = new BigDecimal(amountString);
        BigDecimal amountMonetized = amount.multiply(Constants.appMonetization);
        amountMonetized = amountMonetized.setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalAmount = amount.add(amountMonetized);
        balance = balance.subtract(finalAmount);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
          return log.insufficientBankBalance(methodName);
        } else {
          Transaction transaction = new Transaction();
          transaction.setDate(date);
          transaction.setDebitUserId(user.getId());
          transaction.setCreditUserId(contact.getId());
          transaction.setDescription(description);
          transaction.setFare(amount);
          transaction.setMonetizedFare(amountMonetized);
          userService.makeTransaction(transaction);
          addTransaction(transaction);
          return log.paymentDone(methodName);
        }
      }
    } catch (Exception e) {
      return log.throwAnException(DataManipulationUtils.getCurrentMethodName());
    }
  }
}