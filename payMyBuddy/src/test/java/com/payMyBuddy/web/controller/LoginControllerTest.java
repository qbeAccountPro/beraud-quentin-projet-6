package com.paymybuddy.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoginControllerTest {

  @Autowired
  private LoginController loginController;

  @Autowired
  private MockMvc mvc;

  @Test
  public void shouldReturnDefaultMessage() throws Exception {
    mvc.perform(get("/login")).andDo(print()).andExpect(status().isOk());
  }

  // TODO : ENCODE PASSWORD
  @Test
  public void userLoginTest() throws Exception {
    mvc.perform(formLogin("/login").user("quentin").password("beraud")).andExpect(authenticated());
  }

  @Test

  public void userLoginFailed() throws Exception {
    mvc.perform(formLogin("/login").user("user").password("wrongpassword")).andExpect(unauthenticated());
  }

  @Test
  void testGetAdmin() {

  }

  @Test
  void testGetUser() {

  }

  @Test
  void testGetUserInfo() {

  }

  @Test
  void contextLoads() throws Exception {
    assertThat(loginController).isNotNull();
  }
}
