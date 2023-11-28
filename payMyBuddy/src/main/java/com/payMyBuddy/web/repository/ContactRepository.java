package com.paymybuddy.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface ContactRepository extends JpaRepository<Contact, Integer> {

  /**
   * Some javadoc.
   * 
   * Get for a specific user from this id, all this contacts.
   * 
   * @param urserId the identifier of user.
   * 
   * @return a list of "contact" entity. 
   */
  @Query("SELECT c FROM Contact c WHERE c.user1Id = :userId OR c.user2Id = :userId")
  List<Contact> findContactByUserId(@Param("userId") int userId);

  Contact findFirstByOrderByIdDesc();

}