package com.paymybuddy.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.paymybuddy.web.controller.ProfileBankController;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProfileBankControllerIT {

  @Autowired
  ProfileBankController profileBankController;

  private MockMvc mvc;

  @BeforeEach
  void setup() {
    this.mvc = MockMvcBuilders
        .standaloneSetup(profileBankController)
        .setControllerAdvice()
        .build();
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void creditAccountTest() throws Exception {
    String amount = "1";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/profileBank/creditAccount")
        .param("amountPMB", amount)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "Account credited";

    assertEquals(expectedContent, actualContent);

  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void debitAccountTest() throws Exception {
    String amount = "1";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/profileBank/debitAccount")
        .param("amountBank", amount)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "Account debited";

    assertEquals(expectedContent, actualContent);

  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void debitAccountWithInsuficientBankBalanceTest() throws Exception {
    String amount = "1000000";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/profileBank/debitAccount")
        .param("amountBank", amount)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "bankBalanceInsufficient";

    assertEquals(expectedContent, actualContent);

  }
}
