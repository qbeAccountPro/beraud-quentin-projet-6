package com.paymybuddy.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.web.model.Contact;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.repository.ContactRepository;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

  @Mock
  ContactRepository contactRepositoryMock;

  @Mock
  UserService userServiceMock;

  @InjectMocks
  ContactService contactService = new ContactService();

  @Test
  void testGetAllContactIdForAnUser() {
    // Arrange
    List<Contact> contacts = new ArrayList<>();
    Contact c1 = new Contact(1, 1, 2);
    Contact c2 = new Contact(1, 1, 3);
    contacts.add(c1);
    contacts.add(c2);
    User currentUser = new User();
    currentUser.setId(1);

    when(contactRepositoryMock.findContactByUserId(currentUser.getId())).thenReturn(contacts);

    // Act
    List<Integer> acutalResult = contactService.getAllContactIdForAnUser(currentUser);

    // Assert
    List<Integer> expectedResult = Arrays.asList(2, 3);
    assertEquals(expectedResult, acutalResult);
  }

  @Test
  void testGetContactMails() {
    // Arrange
    User currentUser = new User();
    currentUser.setId(1);

    User contactUser1 = new User();
    contactUser1.setMail("mail1");
    User contactUser2 = new User();
    contactUser2.setMail("mail2");

    List<User> contactUsers = Arrays.asList(contactUser1, contactUser2);

    when(userServiceMock.getListUserById(contactService.getAllContactIdForAnUser(currentUser))).thenReturn(contactUsers);
    // Act
    List<String> acutalResult = contactService.getContactMails(currentUser);

    // Assert
    List<String> expectedResult = Arrays.asList(contactUser1.getMail(), contactUser2.getMail());
    assertEquals(expectedResult, acutalResult);
  }
}
