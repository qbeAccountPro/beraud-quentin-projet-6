package com.paymybuddy.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public List<Transaction> getAllTransactionForAnUser(User user) {
    return transactionRepository.findTransactionByUserId(user.getId());
  }
}