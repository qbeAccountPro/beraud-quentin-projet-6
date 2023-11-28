package com.paymybuddy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Some javadoc :
 * 
 * This class represent the controller of error.
 */
@Controller
public class ErrorController {

  /**
   * Some javadoc :
   * 
   * This method represent the request mapping of coming soon.
   * 
   * @return the compingsoon page.
   */
  @RequestMapping("/comingsoon")
  public String notFoundController() {
    return "comingsoon";
  }
}