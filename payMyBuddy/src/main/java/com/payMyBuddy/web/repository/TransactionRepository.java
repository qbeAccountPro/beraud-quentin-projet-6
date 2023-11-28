package com.paymybuddy.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.Transaction;

/**
 * Some javadoc.
 * 
 * Repository interface for managing Transaction entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for
 * Transaction objects.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

  /**
   * Some javadoc.
   * 
   * Get for a specific user from this id, all this transactions.
   * 
   * @param urserId the identifier of user.
   * 
   * @return the list of transaction.
   */
  @Query("SELECT c FROM Transaction c WHERE c.creditUserId = :userId OR c.debitUserId = :userId")
  List<Transaction> findTransactionByUserId(@Param("userId") int userId);
}