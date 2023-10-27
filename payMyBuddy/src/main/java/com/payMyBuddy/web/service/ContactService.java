package com.paymybuddy.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.model.Contact;
import com.paymybuddy.web.model.User;
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
   * Get all contact for an specific user id.
   * 
   * @param idUser : it's the ID of the specific user.
   *
   */
  public List<Integer> getAllContactIdForAnUser(User user) {
    List<Contact> contacts = contactRepository.findContactByUserId(user.getId());
    List<Integer> listIdContacts = new ArrayList<>();

    for (Contact contact : contacts) {
      if (contact.getUser_1_id() != user.getId()) {
        listIdContacts.add(contact.getUser_1_id());
      } else if (contact.getUser_2_id() != user.getId()) {
        listIdContacts.add(contact.getUser_2_id());
      }
    }
    return listIdContacts;
  }
}