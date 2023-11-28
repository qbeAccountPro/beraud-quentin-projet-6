package com.paymybuddy.web.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paymybuddy.web.controller.TransferController;
import com.paymybuddy.web.repository.ContactRepository;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TransferControllerPayIT {

  @Autowired
  private TransferController transferController;

  @Autowired
  ContactRepository contactRepository;

  private MockMvc mvc;

  @BeforeEach
  void setup() {
    this.mvc = MockMvcBuilders
        .standaloneSetup(transferController)
        .setControllerAdvice()
        .build();
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void payTest() throws Exception {
    String date = "2023-11-28 10:00:00";
    String amount = "20";
    String connection = "jane.smith@gmail.com";
    String description = "Foot avec les copains";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/pay")
        .param("date", date)
        .param("amount", amount)
        .param("connection", connection)
        .param("description", description)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "payDone";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void payWithEmptyDescriptionTest() throws Exception {
    String date = "2023-11-28 10:00:00";
    String amount = "20";
    String connection = "WrongMailInexistent@example.com";
    String description = "";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/pay")
        .param("date", date)
        .param("amount", amount)
        .param("connection", connection)
        .param("description", description)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "emptyDescription";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void payWithInsuficientBalanceTest() throws Exception {
    String date = "2023-11-28 10:00:00";
    String amount = "3000";
    String connection = "WrongMailInexistent@example.com";
    String description = "Foot avec les copains";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/pay")
        .param("date", date)
        .param("amount", amount)
        .param("connection", connection)
        .param("description", description)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "bankBalanceInsufficient";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void payThrowAnExceptionTest() throws Exception {
    String date = "2023-11-28 10:00:00";
    String amount = "1";
    String connection = "WrongMailInexistent@example.com";
    String description = "Foot avec les copains";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/pay")
        .param("date", date)
        .param("amount", amount)
        .param("connection", connection)
        .param("description", description)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "Exception";

    assertEquals(expectedContent, actualContent);
  }
}