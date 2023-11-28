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
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

  /**
   * Some javadoc.
   * 
   * This method represent a request of "BankAccount" entity for a specific "id".
   * 
   * @param id it's identifier.
   * 
   * @return BankAccount entity for a specific id.
   */
  BankAccount findByuserId(int id);
}
