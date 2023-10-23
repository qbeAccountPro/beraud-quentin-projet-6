package com.paymybuddy.web.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

  @Query("SELECT * FROM Transaction WHERE credit_user_id = :userId OR debit_user_id = :userId")
  List<Transaction> findTransactionByUserId(@Param("userId") int userId);

}