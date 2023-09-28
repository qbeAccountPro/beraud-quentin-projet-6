package com.paymybuddy.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.model.User;
import com.paymybuddy.web.repository.UserRepository;

/**
 * Some javadoc.
 * 
 * Service class responsible for User entities.
 * This class contains business logic and serves as an intermediary between
 * controllers and repositories.
 */
@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a collection of Users.
   */
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  /*
   * Some javadoc.
   * 
   * Retrieves and returns a User by id.
   * 
   * @param id : id to retrieves.
   * 
   * @return an optional User.
   */
  public Optional<User> getUserById(Integer id) {
    return userRepository.findById(id);
  }

  /*
   * Some javadoc.
   * 
   * Save a User object.
   * 
   * @param user : User object to save.
   * 
   * @return a User.
   */
  public User addUser(User user) {
    return userRepository.save(user);
  }

  /*
   * Some javadoc.
   * 
   * Delete a User object by id.
   * 
   * @param user : User object to delete.
   */
  public void deleteUser(User user) {
    userRepository.deleteById(user.getId());
  }

  /*
   * Some javadoc.
   * 
   * Update a User object.
   * 
   * @param user : User object to update.
   */
  public void updateUser(User user) {
    Optional<User> foundUser = userRepository.findById(user.getId());
    if (foundUser.isPresent()) {
      addUser(user);
    }
  }
}