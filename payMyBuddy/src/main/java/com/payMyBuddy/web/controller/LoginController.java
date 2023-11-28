package com.paymybuddy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Some javadoc :
 * 
 * This class represent the controller of Login.
 */
@Controller
public class LoginController {

  /**
   * Some javadoc :
   * 
   * This method represent the mapping of Login.
   * 
   * @return the login page.
   */
  @GetMapping("/login")
  String login() {
    return "login";
  }
}