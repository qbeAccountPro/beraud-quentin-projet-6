package com.paymybuddy.web.logging;

import org.springframework.http.ResponseEntity;
import org.tinylog.Logger;

import com.paymybuddy.web.httpResponse.ResponseBuilder;
import com.paymybuddy.web.model.User;

/* 
 * Some javadoc.
 * 
 * This class represent all log generated during the endpoint request.
 */
public class EndpointsLogger {

  private ResponseBuilder response = new ResponseBuilder();

  /**
   * Some javadoc.
   * 
   * Logs a request for the given method.
   * 
   * @param methodName
   */
  public void request(String methodName) {
    Logger.info("Request : " + methodName + ".");
  }

  /**
   * Some javadoc.
   * 
   * Logs a request with an argument for the given method.
   * 
   * @param methodName
   * @param argument   The argument value.
   */
  public void request(String methodName, String argument) {
    Logger.info("Request : " + methodName + " with this argument : " + argument + ".");
  }

  /**
   * Some javadoc.
   * 
   * Logs a request with four arguments for the given method.
   * 
   * @param methodName
   * @param argument1  The first argument value.
   * @param argument2  The second argument value.
   * @param argument3  The third argument value.
   * @param argument4  The fourth argument value.
   */
  public void request(String methodName, String argument1, String argument2, String argument3, String argument4) {
    Logger.info("Request : " + methodName + " with this arguments : " + argument1 + " & " + argument2 + " & "
        + argument3 + " & " + argument4 + ".");
  }

  /**
   * Some javadoc.
   * 
   * Logs a debit or credit on account user.
   * 
   * @param methodName
   * @param amount
   * @param user
   */
  public void request(String methodeName, String amount, User user) {
    Logger.info(
        "Request : " + methodeName + " with this argument : " + amount + " and user email :" + user.getMail() + ".");
  }

  /**
   * Some javadoc.
   * 
   * Logs an empty mail added.
   * 
   * @param methodName
   * @param mail       with emty value.
   * 
   * @return response indicating empty mail.
   */
  public ResponseEntity<String> emptyMail(String methodName, String mail) {
    Logger.info("Answer : " + methodName + ", with empty mail.");
    return response.emptyMail();
  }

  /**
   * Some javadoc.
   * 
   * Logs nonexistent mail in the database.
   * 
   * @param methodName
   * @param mail
   * @return response indicating nonexisting mail.
   */
  public ResponseEntity<String> nonexistentMail(String methodName, String mail) {
    Logger.info("Answer : " + methodName + ", mail doesn't exist in the database : " + mail + ".");
    return response.nonexistentMail();
  }

  /**
   * Some javadoc.
   * 
   * Logs current user mail.
   * 
   * @param methodName
   * @param mail
   * @return response indicating mail come from current user.
   */
  public ResponseEntity<String> currentUserMail(String methodName, String mail) {
    Logger.info("Answer : " + methodName + ", mail is from current user : " + mail + ".");
    return response.currentUserMail();
  }

  /**
   * Some javadoc.
   * 
   * Logs mail come from contact list.
   * 
   * @param methodName
   * @param mail
   * @return response indicating existing contact.
   * 
   */
  public ResponseEntity<String> existingContactMail(String methodName, String mail) {
    Logger.info("Answer : " + methodName + ", mail is already inside the contact list : " + mail + ".");
    return response.existingContactMail();
  }

  /**
   * Some javadoc.
   * 
   * Logs successffuly added mail in the contact list.
   * 
   * @param methodName
   * @param mail
   * @return response indicating successffully contact added.
   */
  public ResponseEntity<String> successffulyContactMailAdded(String methodName, String mail) {
    Logger.info("Answer : " + methodName + ", mail successffuly added : " + mail + ".");
    return response.MailAdded();
  }

  /**
   * Some javadoc.
   * 
   * Logs an insufficient bank balance.
   * 
   * @param methodName
   * @return response indicating inssufficient bank balance.
   */
  public ResponseEntity<String> insufficientBankBalance(String methodName) {
    Logger.info("Answer : " + methodName + ", insufficient bank balance.");
    return response.insufficientBankBalance();

  }

  /**
   * Some javadoc.
   * 
   * Logs payment done.
   * 
   * @param methodName
   * @return response indicating successffuly payment done.
   */
  public ResponseEntity<String> paymentDone(String methodName) {
    Logger.info("Answer : " + methodName + ", payement done.");
    return response.paymentDone();
  }

  /**
   * Some javadoc.
   * 
   * Logs an empty description inside the transaction.
   * 
   * @param methodName
   * @return response an empty description inside the transaction.
   */
  public ResponseEntity<String> emptyDescription(String methodName) {
    Logger.info("Answer : " + methodName + ", the description is empty.");
    return response.emptyDescription();
  }

  /**
   * Some javadoc.
   * 
   * Logs an exception.
   * 
   * @param methodName
   * @return response indicating throw an exception.
   */
  public ResponseEntity<String> throwAnException(String methodName) {
    Logger.info("Answer : " + methodName + ", throw an exception.");
    return response.throwAnException();
  }

  /**
   * Some javadoc.
   * 
   * Logs the successffuly debited user account.
   * 
   * @param methodName
   * @return response indicating the successffuly debited user account.
   */
  public ResponseEntity<String> accountDebited(String methodName) {
    Logger.info("Answer : " + methodName + ", account debited.");
    return response.accountDebited();
  }

  /**
   * Some javadoc.
   * 
   * Logs the successffuly credited user account.
   * 
   * @param methodName
   * @return response indicating the successffuly credited user account.
   */
  public ResponseEntity<String> accountCredited(String methodName) {
    Logger.info("Answer : " + methodName + ", account credited.");
    return response.accountCredited();
  }
}