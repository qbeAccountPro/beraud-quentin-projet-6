package com.paymybuddy.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoginControllerTest {

  @Autowired
  private LoginController controller;

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
    assertThat(controller).isNotNull();
  }
}
