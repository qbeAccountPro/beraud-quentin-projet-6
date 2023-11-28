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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paymybuddy.web.controller.TransferController;
import com.paymybuddy.web.model.Contact;
import com.paymybuddy.web.repository.ContactRepository;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class TransferControllerAddConnectionIT {

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
  public void addConnectionWithInexitingEmailTest() throws Exception {
    String mail = "WrongMailInexistent@example.com";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/addConnection")
        .param("mail", mail)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "mail-nonexistent";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void addConnectionWithExistingContactMail() throws Exception {
    String mail = "jane.smith@gmail.com";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/addConnection")
        .param("mail", mail)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "mail-FromContactList";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void addConnectionWithEmptyEmailTest() throws Exception {
    String mail = "";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/addConnection")
        .param("mail", mail)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "mail-empty";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void addConnectionWithCurrentUserMailTest() throws Exception {
    String mail = "john.doe@gmail.com";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/addConnection")
        .param("mail", mail)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "mail-FromCurrentUser";

    assertEquals(expectedContent, actualContent);
  }

  @Test
  @Transactional
  @WithMockUser(username = "john.doe@gmail.com", password = "{bcrypt}$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS", roles = "USER")
  public void addConnectionTest() throws Exception {
    String mail = "bruce.wayne@corp.com";

    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/transfer/addConnection")
        .param("mail", mail)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String actualContent = result.getResponse().getContentAsString();
    String expectedContent = "mail-Added";

    assertEquals(expectedContent, actualContent);

    Contact contact = contactRepository.findFirstByOrderByIdDesc();

    contactRepository.delete(contact);
  }
}
