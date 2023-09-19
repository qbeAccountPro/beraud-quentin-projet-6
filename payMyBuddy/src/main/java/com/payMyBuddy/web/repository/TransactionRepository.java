package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
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
public interface TransactionRepository extends CrudRepository<Transaction, Integer>{

}