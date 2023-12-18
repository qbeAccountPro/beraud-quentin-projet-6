package com.paymybuddy.web.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.paymybuddy.web.constants.Constants;
import com.paymybuddy.web.logging.EndpointsLogger;
import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.Contact;
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
  UserRepository userRepository;

  @Autowired
  BankAccountService bankAccountService;

  @Autowired
  ContactService contactService;

  EndpointsLogger log = new EndpointsLogger();

  /**
   * Some javadoc.
   * 
   * @return collection of "user" entites.
   */
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Some javadoc.
   * 
   * Retrieves and returns a "user" entity by id.
   * 
   * @param id : id to retrieves.
   * 
   * @return an optional User.
   */
  public User getUserById(int id) {
    return userRepository.findById(id);
  }

  /**
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

  /**
   * Some javadoc.
   * 
   * Delete a User object by id.
   * 
   * @param user : User object to delete.
   */
  public void deleteUser(User user) {
    userRepository.deleteById(user.getId());
  }

  /**
   * Some javadoc.
   * 
   * Get a list of "user" entities from a list of contact containing user
   * identifiers.
   * 
   * @param contactsId it's a list of identifiers from "user" entity.
   * 
   * @Return List of "user" entities.
   */
  public List<User> getListUserById(List<Integer> contactsId) {
    return userRepository.findAllByIdIn(contactsId);
  }

  /**
   * Some Javadoc.
   * 
   * Retrieves a user by email.
   * 
   * @param mail of the user.
   * 
   * @return "user" entity.
   */
  public User getUserByMail(String mail) {
    return userRepository.findByMail(mail);
  }

  /**
   * Some javadoc.
   * 
   * This method provide the transaction betwen two users.
   * 
   * @param t represent the current transaction.
   */
  public void makeTransaction(Transaction t) {
    User debitUser = getUserById(t.getDebitUserId());
    User creditUser = getUserById(t.getCreditUserId());

    BigDecimal debitBankBalance = debitUser.getBankBalance();
    BigDecimal creditBankBalance = creditUser.getBankBalance();
    BigDecimal fare = t.getFare();

    debitBankBalance = debitBankBalance
        .subtract(fare.add(fare.multiply(Constants.appMonetization))).setScale(2, RoundingMode.HALF_UP);

    creditBankBalance = creditBankBalance.add(fare).setScale(2, RoundingMode.HALF_UP);;

    userRepository.updateBankBalance(debitUser.getId(), debitBankBalance);
    userRepository.updateBankBalance(creditUser.getId(), creditBankBalance);
  }

  /**
   * Some javadoc.
   * 
   * This method is used to credit user account. (PayMyBuddy)
   * 
   * @param user      entity to be credited.
   * @param amountPMB the corresponding amount to be credited.
   * @return a response confirming or not the transaction.
   */
  public ResponseEntity<String> creditUserAccount(User user, String amountPMB, String methodName) {
    try {
      BigDecimal creditAmount = new BigDecimal(amountPMB);
      BigDecimal userBalance = user.getBankBalance();
      BigDecimal finalBalance = userBalance.add(creditAmount);
      userRepository.updateBankBalance(user.getId(), finalBalance);
      return log.accountCredited(methodName);
    } catch (Exception e) {
      return log.throwAnException(methodName);
    }
  }

  /**
   * Some javadoc.
   * 
   * This method is used to debit user account. (PayMyBuddy)
   * 
   * @param user       entity to be debited.
   * @param amountBank the corresponding amount to be debited.
   * @return a response confirming or not the transaction.
   */
  public ResponseEntity<String> debitUserAccount(User user, String amountBank, String methodName) {
    try {
      BigDecimal debitAmount = new BigDecimal(amountBank);
      BigDecimal userBalance = user.getBankBalance();
      if (userBalance.compareTo(debitAmount) < 0) {
        return log.insufficientBankBalance(methodName);
      }
      BigDecimal finalBalance = userBalance.subtract(debitAmount);
      userRepository.updateBankBalance(user.getId(), finalBalance);
      return log.accountDebited(methodName);
    } catch (Exception e) {
      return log.throwAnException(methodName);
    }
  }

  /**
   * Some javadoc :
   * 
   * This method retrieves the current user. It will get the current
   * authentication user entity.
   * 
   * @return the user object.
   */
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String mail = userDetails.getUsername();
      return getUserByMail(mail);
    } else if (authentication.getPrincipal() instanceof OAuth2User) {
      OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
      String email = oauth2User.getAttribute("email");
      if (email != null) {
        User user = getUserByMail(email);
        if (user != null) {
          return user;
        } else {
          user = new User();
          user.setMail(email);
          user.setBankBalance(new BigDecimal("0"));
          user.setFirstName(oauth2User.getAttribute("given_name"));
          user.setLastName(oauth2User.getAttribute("family_name"));
          User userCreated = addUser(user);
          BankAccount bankAccount = new BankAccount();
          bankAccount.setUserId(userCreated.getId());
          bankAccount.setBankName("Fake bank");
          bankAccount.setIBAN(000);
          bankAccountService.addBankAccount(bankAccount);
          return user;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Some javadoc :
   * 
   * This method check the email input. It will verify if the email is correct and
   * nonexistent inside the contact user.
   * 
   * @param mail  to check.
   * @param model of the HTML page.
   * @return the response entity to confirming or not the email.
   */
  public ResponseEntity<String> checkEmail(String mail, Model model, String methodName) {
    if (mail.isEmpty()) {
      return log.emptyMail(methodName, mail);
    } else {
      User contact = getUserByMail(mail);
      if (contact == null) {
        return log.nonexistentMail(methodName, mail);
      } else {
        User user = getCurrentUser();
        String mailUser = user.getMail();
        if (mailUser.equals(mail)) {
          return log.currentUserMail(methodName, mail);
        } else {
          List<Integer> userContacts = contactService.getAllContactIdForAnUser(user);
          boolean emailFromContact = userContacts.contains(contact.getId());
          if (emailFromContact) {
            return log.existingContactMail(methodName, mail);
          } else {
            Contact newContact = new Contact();
            newContact.setUser1Id(user.getId());
            newContact.setUser2Id(contact.getId());
            contactService.addContact(newContact);
            model.addAttribute("contacts", contactService.getContactMails(user));
            return log.successffulyContactMailAdded(methodName, mail);
          }
        }
      }
    }
  }
}