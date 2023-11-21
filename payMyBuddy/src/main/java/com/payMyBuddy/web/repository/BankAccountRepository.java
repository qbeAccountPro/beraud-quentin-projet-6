package com.paymybuddy.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer>{

  BankAccount findByuserId(int id);
  
}
