package com.paymybuddy.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.paymybuddy.web.dto.MyTransactionDto;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.service.BankAccountService;
import com.paymybuddy.web.service.ContactService;
import com.paymybuddy.web.service.TransactionService;
import com.paymybuddy.web.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

  @Mock
  UserService userServiceMock;

  @Mock
  ContactService contactServiceMock;

  @Mock
  TransactionService transactionServiceMock;

  @Mock
  BankAccountService bankAccountServiceMock;

  @Mock
  Model modelMock;

  @InjectMocks
  TransferController transferController = new TransferController();

  @Test
  void testAddConnection() {
    // Arrange
    String mail = "mail@gmail.com";

    // Act
    transferController.addConnection(mail, modelMock);

    // Assert
    verify(userServiceMock, times(1)).checkEmail(mail, modelMock, "addConnection");
  }

  @Test
  void testLoadTranfer() {
    // Arrange
    User user = new User(1, "Quentin", "Beraud", new BigDecimal("1000"), "qbe@gmail.com", "null");
    List<MyTransactionDto> allTransactionDto = new ArrayList<>();
    List<String> allContact = new ArrayList<>();
    when(userServiceMock.getCurrentUser()).thenReturn(user);
    when(contactServiceMock.getContactMails(user)).thenReturn(allContact);
    when(transactionServiceMock.getTransactionsDto(user)).thenReturn(allTransactionDto);

    // Act
    String actualResult = transferController.loadTransfer(modelMock);
    // Assert
    String expectedResult = "transfer";
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testLoadTranferWithInexistingUser() {
    // Arrange
    when(userServiceMock.getCurrentUser()).thenReturn(null);
    // Act
    String actualResult = transferController.loadTransfer(modelMock);
    // Assert
    String expectedResult = "error";
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testMakeTransaction() throws ParseException {
    // Arrange
    String date = "2022-10-10 10:00:00", amount = "100", connection = "contact@emai.com", description = "description";
    // Act
    transferController.makeTransaction(date, amount, connection, description, modelMock);

    // Assert
    verify(transactionServiceMock, times(1)).makeTransaction(date, amount, connection, description, modelMock);
  }
}
