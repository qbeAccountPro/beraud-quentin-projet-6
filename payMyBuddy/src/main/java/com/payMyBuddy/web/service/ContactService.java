package com.paymybuddy.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.model.Contact;
import com.paymybuddy.web.repository.ContactRepository;

/**
 * Some javadoc.
 * 
 * Service class responsible for Contact entities.
 * This class contains business logic and serves as an intermediary between
 * controllers and repositories.
 */
@Service
public class ContactService {

  @Autowired
  private ContactRepository contactRepository;

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a collection of Contacts.
   */
  public Iterable<Contact> getContacts() {
    return contactRepository.findAll();
  }

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a Contact by id.
   * 
   * @param id : id to retrieves.
   * 
   * @return an optional Contact.
   */
  public Optional<Contact> getContactById(Integer id) {
    return contactRepository.findById(id);
  }

  /*
   * Some javadoc.
   * 
   * Save a Contact object.
   * 
   * @param contact : Contact object to save.
   * 
   * @return a Contact.
   */
  public Contact addContact(Contact contact) {
    return contactRepository.save(contact);
  }

  /*
   * Some javadoc.
   * 
   * Delete a Contact object by id.
   * 
   * @param contact : Contact object to delete.
   *
   */
  public void deleteContact(Contact contact) {
    contactRepository.deleteById(contact.getId());
  }

  /*
   * Some javadoc.
   * 
   * Update a Contact object.
   * 
   * @param contact : Contact object to update.
   *
   */
  public void updateContact(Contact contact) {
    Optional<Contact> foundContact = contactRepository.findById(contact.getId());
    if (foundContact.isPresent()) {
      addContact(contact);
    }
  }
}