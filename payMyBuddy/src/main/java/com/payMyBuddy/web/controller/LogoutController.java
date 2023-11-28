package com.paymybuddy.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Some javadoc :
 * 
 * This class represent the controller of logout.
 */
@Controller
public class LogoutController {

  /**
   * Some javadoc :
   * 
   * This method represent the mapping of logout.
   * 
   * @param request  the HTTP servlet request
   * @param response the HTTP servlet response
   * 
   * @return the login page from logout.
   */
  @RequestMapping("/logout")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return "redirect:/login?logout";
  }
}
