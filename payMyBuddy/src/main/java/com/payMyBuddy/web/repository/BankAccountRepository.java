package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.BankAccount;

/**
 * Some javadoc.
 * 
 * Repository interface for managing BankAccount entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for
 * BankAccount objects.
 */
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer>{
  
}
