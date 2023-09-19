package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.Identifier;

/**
 * Some javadoc.
 * 
 * Repository interface for managing Identifier entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for
 * Identifier objects.
 */
@Repository
public interface IdentifierRepository extends CrudRepository<Identifier, Integer>{
  
}
