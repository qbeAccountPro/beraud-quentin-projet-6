package com.paymybuddy.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.paymybuddy.web.dto.MyTransactionDto;
import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

  @Mock
  TransactionRepository transactionRepositoryMock;

  @Mock
  private UserService userServiceMock;

  @Mock
  ContactService contactServiceMock;

  @Mock
  Model modelMock;

  @InjectMocks
  private TransactionService transactionService = new TransactionService();

  String date, amount, connection, description;

  User currentUser, contactUser;

  @BeforeEach
  void setUp() {
    date = "2023-09-18 20:00:12";
    amount = "100";
    connection = "contact@email.com";
    description = "description";

    currentUser = new User();
    currentUser.setId(1);
    currentUser.setFirstName("Quentin");
    currentUser.setLastName("Beraud");
    currentUser.setBankBalance(new BigDecimal("200"));
    currentUser.setMail("mail@gmail.com");
    currentUser.setPassword("PaSsWoRd");

    contactUser = new User();
    contactUser.setId(2);
    contactUser.setFirstName("Boby");
    contactUser.setLastName("Johny");
    contactUser.setBankBalance(new BigDecimal("9999"));
    contactUser.setMail(connection);
    contactUser.setPassword("PaSsWoRd");
  }

  @Test
  void testMakeTransaction() {
    // Arrange
    when(userServiceMock.getUserByMail(connection)).thenReturn(contactUser);
    when(userServiceMock.getCurrentUser()).thenReturn(currentUser);

    // Act
    ResponseEntity<String> actualResult = transactionService.makeTransaction(date, amount, connection, description,
        modelMock);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("payDone");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testMakeTransactionTrhowAnException() {
    // Arrange
    doThrow(new RuntimeException("Message d'erreur")).when(userServiceMock).getCurrentUser();

    // Act
    ResponseEntity<String> actualResult = transactionService.makeTransaction(date, amount, connection, description,
        modelMock);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("Exception");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testMakeTransactionInsuficientBankBalance() {
    // Arrange
    when(userServiceMock.getUserByMail(connection)).thenReturn(contactUser);
    when(userServiceMock.getCurrentUser()).thenReturn(currentUser);

    // Act
    ResponseEntity<String> actualResult = transactionService.makeTransaction(date, "10000", connection, description,
        modelMock);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("bankBalanceInsufficient");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testConvertTransactionsIntoDto() throws Exception {
    // Arrange
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = format.parse("2002-10-10 10:00:20");
    Transaction t1 = new Transaction(1, 2, 1, description, new BigDecimal("100"), new BigDecimal("0.5"), date);
    Transaction t2 = new Transaction(2, 1, 2, description, new BigDecimal("200"), null, date);
    List<Transaction> ts = new ArrayList<>();
    ts.add(t1);
    ts.add(t2);
    MyTransactionDto mt1 = new MyTransactionDto("Johny", "Boby", description, t1.getFare().negate(),
        t1.getMonetizedFare().negate(),
        date);
    MyTransactionDto mt2 = new MyTransactionDto("Johny", "Boby", description, t2.getFare(), t2.getMonetizedFare(),
        date);
    List<MyTransactionDto> expectedResult = new ArrayList<>();
    expectedResult.add(mt1);
    expectedResult.add(mt2);

    when(userServiceMock.getUserById(2)).thenReturn(contactUser);

    // Act
    List<MyTransactionDto> actualResult = transactionService.convertTransactionsIntoDto(ts, currentUser);

    // Assert
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testGetTransactionsDto() throws Exception {
    // Arrange
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = format.parse("2002-10-10 10:00:20");
    Transaction t1 = new Transaction(1, 2, 1, description, new BigDecimal("100"), new BigDecimal("0.5"), date);
    Transaction t2 = new Transaction(2, 1, 2, description, new BigDecimal("200"), null, date);
    List<Transaction> ts = new ArrayList<>();
    ts.add(t1);
    ts.add(t2);
    MyTransactionDto mt1 = new MyTransactionDto("Johny", "Boby", description, t1.getFare().negate(),
        t1.getMonetizedFare().negate(),
        date);
    MyTransactionDto mt2 = new MyTransactionDto("Johny", "Boby", description, t2.getFare(), t2.getMonetizedFare(),
        date);
    List<MyTransactionDto> expectedResult = new ArrayList<>();
    expectedResult.add(mt1);
    expectedResult.add(mt2);

    when(transactionRepositoryMock.findTransactionByUserId(currentUser.getId())).thenReturn(ts);
    when(userServiceMock.getUserById(2)).thenReturn(contactUser);

    // Act
    List<MyTransactionDto> actualResult = transactionService.getTransactionsDto(currentUser);

    // Assert
    assertEquals(expectedResult, actualResult);
  }
}
