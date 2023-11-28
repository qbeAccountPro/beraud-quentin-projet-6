package com.paymybuddy.web.communUtilts;


/**
 * Some javadoc.
 * 
 * This class contains different utility methods for modified or checking
 * strings.
 */
public class DataManipulationUtils {

  /**
   * Some javadoc.
   * Retrieves the name of the current method being executed.
   *
   * @return The name of the current method.
   */
  public static String getCurrentMethodName() {
    StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
    return stackTraceElement.getMethodName();
  }
}