package com.paymybuddy.web.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.dto.MyTransactionDto;
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
  private TransactionRepository transactionRepository;

  @Autowired
  private UserService userService;

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a collection of transactions.
   */
  public Iterable<Transaction> getTransactions() {
    return transactionRepository.findAll();
  }

  /*
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

  /*
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

  /*
   * Some javadoc.
   * 
   * Delete a Transaction object by id.
   * 
   * @param transaction : Transaction object to delete.
   */
  public void deleteTransaction(Transaction transaction) {
    transactionRepository.deleteById(transaction.getId());
  }

  /*
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

  /*
   * Some javadoc.
   * 
   * Get transactionsDto from an User.
   * 
   * @param user : user object.
   */
  public List<MyTransactionDto> getTransactionsDto(User user) {
    List<Transaction> transactions = transactionRepository.findTransactionByUserId(user.getId());
    return convertTransactionsIntoDto(transactions, user);
  }

  private List<MyTransactionDto> convertTransactionsIntoDto(List<Transaction> transactions, User user) {
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

  private MyTransactionDto getTransactionDataForDto(Transaction transaction, boolean creditFare) {
    MyTransactionDto myTransactionDto = new MyTransactionDto();
    myTransactionDto.setMonetizedFare(transaction.getMonetizedFare());
    myTransactionDto.setDescription(transaction.getDescription());
    myTransactionDto.setDate(transaction.getDate());
    if (creditFare) {
      myTransactionDto.setFare(transaction.getFare());
    } else {
      myTransactionDto.setFare(transaction.getFare().negate());
    }
    return myTransactionDto;
  }
}