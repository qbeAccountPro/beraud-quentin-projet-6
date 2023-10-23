package com.paymybuddy.web.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.Contact;

/**
 * Some javadoc.
 * 
 * Repository interface for managing Contact entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for
 * Contact objects.
 */
@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

  /**
   * Some javadoc.
   * 
   * Get for a specific user from this id, all this contacts.
   */
  @Query("SELECT * FROM Contact WHERE user_1_Id = :userId OR user_2_Id = :userId")
  List<Contact> findContactByUserId(@Param("userId") int userId);
}
