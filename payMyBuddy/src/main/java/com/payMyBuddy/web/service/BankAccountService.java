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
 * controllers and repository.
 */
@Service
public class BankAccountService {

  @Autowired
  BankAccountRepository bankAccountRepository;

  /**
   * Some javadoc.
   * 
   * Retrieves and returns a "BankAccount" entity by id.
   * 
   * @param id to retrieves.
   * 
   * @return an optional "BankAccount" entity.
   */
  public Optional<BankAccount> getBankAccountById(Integer id) {
    return bankAccountRepository.findById(id);
  }

  /**
   * Some javadoc.
   * 
   * Save a BankAccount object.
   * 
   * @param bankAccount entity to save.
   * 
   * @return a "BankAccount" entity saved with the corresponding identifier.
   */
  public BankAccount addBankAccount(BankAccount bankAccount) {
    return bankAccountRepository.save(bankAccount);
  }

  /**
   * Some javadoc.
   * 
   * Find a BankAccount object from user object.
   * 
   * @param user : "user" object to find.
   * 
   */
  public BankAccount findBankAccountByUser(User user) {
    return bankAccountRepository.findByuserId(user.getId());
  }
}