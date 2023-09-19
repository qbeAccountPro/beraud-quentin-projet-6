package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
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

}
