package com.paymybuddy.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  ContactRepository contactRepository;

  @Autowired
  UserService userService;

  /**
   * Some javadoc.
   * 
   * @return a collection of "contact" entities.
   */
  public Iterable<Contact> getContacts() {
    return contactRepository.findAll();
  }

  /**
   * Some javadoc.
   * 
   * Retrieves and returns a Contact by id.
   * 
   * @param id : id to retrieves.
   * 
   * @return an optional "contact" entity.
   */
  public Optional<Contact> getContactById(Integer id) {
    return contactRepository.findById(id);
  }

  /**
   * Some javadoc.
   * 
   * Save a Contact object.
   * 
   * @param contact : "contact" entity to save.
   * 
   * @return the corresponding "contact" entity saved with identifier.
   */
  public Contact addContact(Contact contact) {
    return contactRepository.save(contact);
  }

  /**
   * Some javadoc.
   * 
   * Delete a contact object by id.
   * 
   * @param contact : User object to delete.
   */
  public void deleteContact(Contact contact) {
    contactRepository.deleteById(contact.getId());
  }


  /**
   * Some javadoc.
   * 
   * Get all contact identifiers for a specific user.
   * 
   * @param user entity.
   */
  public List<Integer> getAllContactIdForAnUser(User user) {
    List<Contact> contacts = contactRepository.findContactByUserId(user.getId());
    List<Integer> listIdContacts = new ArrayList<>();

    for (Contact contact : contacts) {
      if (contact.getUser1Id() != user.getId()) {
        listIdContacts.add(contact.getUser1Id());
      } else if (contact.getUser2Id() != user.getId()) {
        listIdContacts.add(contact.getUser2Id());
      }
    }
    return listIdContacts;
  }

  /**
   * Some javadoc :
   * 
   * This method get all contact of a specific user.
   * 
   * @param user is the entity of the currently authenticated user
   * 
   * @return a list of each mail contact.
   */
  public List<String> getContactMails(User user) {
    List<User> userContacts = userService.getListUserById(getAllContactIdForAnUser(user));
    return userContacts.stream()
        .map(User::getMail)
        .collect(Collectors.toList());
  }
}