package com.paymybuddy.web.httpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Some javadoc.
 * 
 * Builds consistent HTTP responses for various scenarios.
 * 
 * This class provides methods to generate ResponseEntity instances
 * with appropriate status codes and response bodies.
 */
public class ResponseBuilder {

  /**
   * Some javadoc :
   * 
   * This method return a response entity for an empty mail.
   * 
   * @return ResponseEntity : "mail-empty".
   */
  public ResponseEntity<String> emptyMail() {
    return ResponseEntity.status(HttpStatus.OK).body("mail-empty");
  }

  /**
   * Some javadoc :
   * 
   * @return non existent mail in the database.
   */
  public ResponseEntity<String> nonexistentMail() {
    return ResponseEntity.status(HttpStatus.OK).body("mail-nonexistent");
  }

  /**
   * Some javadoc :
   * 
   * @return current user mail try to be added.
   */
  public ResponseEntity<String> currentUserMail() {
    return ResponseEntity.status(HttpStatus.OK).body("mail-FromCurrentUser");
  }

  /**
   * Some javadoc :
   * 
   * @return mail come from actual contact list try to be added.
   */
  public ResponseEntity<String> existingContactMail() {
    return ResponseEntity.status(HttpStatus.OK).body("mail-FromContactList");
  }

  /**
   * Some javadoc :
   * 
   * @return mail added successffuly.
   */
  public ResponseEntity<String> MailAdded() {
    return ResponseEntity.status(HttpStatus.OK).body("mail-Added");
  }

  /**
   * Some javadoc :
   * 
   * @return insufficient bank banalnce.
   */
  public ResponseEntity<String> insufficientBankBalance() {
    return ResponseEntity.status(HttpStatus.OK).body("bankBalanceInsufficient");
  }

  /**
   * Some javadoc :
   * 
   * @return payment done.
   */
  public ResponseEntity<String> paymentDone() {
    return ResponseEntity.status(HttpStatus.OK).body("payDone");
  }

  /**
   * Some javadoc :
   * 
   * @return empty description.
   */
  public ResponseEntity<String> emptyDescription() {
    return ResponseEntity.status(HttpStatus.OK).body("emptyDescription");
  }

  /**
   * Some javadoc :
   * 
   * @return account debited.
   */
  public ResponseEntity<String> accountDebited() {
    return ResponseEntity.status(HttpStatus.OK).body("Account debited");
  }

  /**
   * Some javadoc :
   * 
   * @return account credited
   */
  public ResponseEntity<String> accountCredited() {
    return ResponseEntity.status(HttpStatus.OK).body("Account credited");
  }

  /**
   * Some javadoc :
   * 
   * @return the method throw an exception.
   */
  public ResponseEntity<String> throwAnException() {
    return ResponseEntity.status(HttpStatus.OK).body("Exception");
  }
}