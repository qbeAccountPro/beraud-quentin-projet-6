package com.paymybuddy.web.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.model.Transaction;
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
  public User getUserById(int id) {
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


  public User getUserByUsername(String username) {
    return userRepository.findByFirstName(username);
  }

  public List<User> getListUserById(List<Integer> contactsId) {
    return userRepository.findAllByIdIn(contactsId);
  }

  public User getUserByMail(String mail) {
    return userRepository.findByMail(mail);
  }


  public void makeTransaction(Transaction t) {
    User debitUser = getUserById(t.getDebitUserId());
    User creditUser = getUserById(t.getCreditUserId());

    BigDecimal debitBankBalance = debitUser.getBankBalance();
    BigDecimal creditBankBalance = creditUser.getBankBalance();

    BigDecimal applicationMonetization = new BigDecimal(0.005); // TODO CREER CONSTANTE
    BigDecimal fare = t.getFare();

    debitBankBalance = debitBankBalance
        .subtract(fare.add(fare.multiply(applicationMonetization)));

    creditBankBalance = creditBankBalance.add(fare);

    userRepository.updateBankBalance(debitUser.getId(), debitBankBalance);
    userRepository.updateBankBalance(creditUser.getId(), creditBankBalance);

    // TODO CODER MAKE A TRANSACTION
  }
}