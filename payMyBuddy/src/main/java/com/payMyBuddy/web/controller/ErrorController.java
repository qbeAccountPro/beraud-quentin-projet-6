package com.paymybuddy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

  @RequestMapping("/comingsoon")
  public String notFoundController() {
    return "comingsoon";
  }
}
