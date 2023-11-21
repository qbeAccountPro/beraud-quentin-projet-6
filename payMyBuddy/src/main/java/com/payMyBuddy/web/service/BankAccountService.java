package com.paymybuddy.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.repository.BankAccountRepository;

/**
 * Some javadoc.
 * 
 * Service class responsible for BankAccount entities.
 * This class contains business logic and serves as an intermediary between
 * controllers and repositories.
 */
@Service
public class BankAccountService {

  @Autowired
  public BankAccountRepository bankAccountRepository;

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a collection of BankAccounts.
   */
  public Iterable<BankAccount> getBankAccounts() {
    return bankAccountRepository.findAll();
  }

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a BankAccount by id.
   * 
   * @param id : id to retrieves.
   * 
   * @return an optional BankAccount.
   */
  public Optional<BankAccount> getBankAccountById(Integer id) {
    return bankAccountRepository.findById(id);
  }

  /*
   * Some javadoc.
   * 
   * Save a BankAccount object.
   * 
   * @param bankAccount : BankAccount object to save.
   * 
   * @return a BankAccount.
   */
  public BankAccount addBankAccount(BankAccount bankAccount) {
    return bankAccountRepository.save(bankAccount);
  }

  /*
   * Some javadoc.
   * 
   * Delete a BankAccount object by id.
   * 
   * @param bankAccount : BankAccount object to delete.
   * 
   */
  public void deleteBankAccount(BankAccount bankAccount) {
    bankAccountRepository.deleteById(bankAccount.getId());
  }

  /*
   * Some javadoc.
   * 
   * Update a BankAccount object.
   * 
   * @param bankAccount : BankAccount object to update.
   * 
   */
  public void updateBankAccount(BankAccount bankAccount) {
    Optional<BankAccount> foundBankAccount = bankAccountRepository.findById(bankAccount.getId());
    if (foundBankAccount.isPresent()) {
      addBankAccount(bankAccount);
    }
  }

  public BankAccount findBankAccountByUser(User user) {
    return bankAccountRepository.findByuserId(user.getId());
  }
}