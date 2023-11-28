package com.paymybuddy.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.Transaction;
import com.paymybuddy.web.model.User;
import com.paymybuddy.web.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepositoryMock;
  @Mock
  private ContactService contactServiceMock;
  @Mock
  private Model modelMock;
  @Mock
  private BankAccountService bankAccountServiceMock;
  @Mock
  private Authentication authenticationMock;

  @InjectMocks
  private UserService userService = new UserService();

  User currentUser, contactUser;
  String methodName;
  BigDecimal balance = new BigDecimal("100");

  String userMail = "user@gmail.com", contactMail = "contact@gmail.com";
  String password = "password";

  @BeforeEach
  void setUp() {
    methodName = "generalMethodName";
    currentUser = new User(1, "Quentin", "Beraud", balance, userMail, password);
    contactUser = new User(2, "George", "Blood", balance, contactMail, password);
    userService.userRepository = userRepositoryMock;
    userService.contactService = contactServiceMock;
  }

  OAuth2User setAuthenticationOAuth2User() {
    // OAuth2User Authentication :
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", userMail);
    attributes.put("given_name", "Quentin");
    attributes.put("family_name", "Beraud");
    Collection<? extends GrantedAuthority> authorities = Collections
        .singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    OAuth2User oauth2User = new DefaultOAuth2User(
        authorities,
        attributes,
        "email");
    return oauth2User;
  }

  Authentication setAuthenticationUserDetails() {
    UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(userMail)
        .password(password)
        .roles("USER")
        .build();
    Authentication authenticationUserDetails = new UsernamePasswordAuthenticationToken(userDetails, password,
        userDetails.getAuthorities());
    return authenticationUserDetails;
  }

  @Test
  void testAddUser() {
    // Arrange
    when(userRepositoryMock.save(currentUser)).thenReturn(currentUser);

    // Act
    User savedUser = userService.addUser(currentUser);

    // Assert
    assertEquals(1, savedUser.getId());
    verify(userRepositoryMock, times(1)).save(currentUser);
  }

  @Test
  void testCheckEmailWithEmptyMail() {
    // Arrange
    userMail = "";

    // Act
    ResponseEntity<String> actualResult = userService.checkEmail(userMail, null, methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("mail-empty");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testCheckEmailWithNullContact() {
    // Arrange
    when(userService.getUserByMail(userMail)).thenReturn(null);

    // Act
    ResponseEntity<String> actualResult = userService.checkEmail(userMail, null, methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("mail-nonexistent");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testCheckEmailWithCurrentUserMail() {
    // Arrange
    SecurityContextHolder.getContext().setAuthentication(setAuthenticationUserDetails());
    when(userService.getUserByMail(userMail)).thenReturn(currentUser);

    // Act
    ResponseEntity<String> actualResult = userService.checkEmail(userMail, null, methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("mail-FromCurrentUser");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testCheckEmailWithEmailFromContact() {
    // Arrange
    SecurityContextHolder.getContext().setAuthentication(setAuthenticationUserDetails());
    List<Integer> userContacts = Arrays.asList(2, 3);

    when(userService.getUserByMail(userMail)).thenReturn(currentUser);
    when(userService.getUserByMail(contactMail)).thenReturn(contactUser);
    when(contactServiceMock.getAllContactIdForAnUser(currentUser)).thenReturn(userContacts);

    // Act
    ResponseEntity<String> actualResult = userService.checkEmail(contactMail, null, methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("mail-FromContactList");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testCheckEmailSuccessffulyAdded() {
    // Arrange
    SecurityContextHolder.getContext().setAuthentication(setAuthenticationUserDetails());
    List<Integer> userContacts = Arrays.asList(3);

    when(userService.getUserByMail(userMail)).thenReturn(currentUser);
    when(userService.getUserByMail(contactMail)).thenReturn(contactUser);
    when(contactServiceMock.getAllContactIdForAnUser(currentUser)).thenReturn(userContacts);

    // Act
    ResponseEntity<String> actualResult = userService.checkEmail(contactMail, modelMock, methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("mail-Added");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testCreditUserAccount() {
    // Act
    ResponseEntity<String> actualResult = userService.creditUserAccount(currentUser, "20", methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("Account credited");
    assertEquals(expectedResult, actualResult);

  }

  @Test
  void testCreditUserAccountThrowAnException() {
    // Arrange
    doThrow(new RuntimeException("Message d'erreur")).when(userRepositoryMock).updateBankBalance(any(int.class),
        any(BigDecimal.class));

    // Act
    ResponseEntity<String> actualResult = userService.creditUserAccount(currentUser, "20", methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("Exception");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testDebitUserAccount() {
    // Act
    ResponseEntity<String> actualResult = userService.debitUserAccount(currentUser, "20", methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("Account debited");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testDebitUserAccountInsufficientBankBalance() {
    // Act
    ResponseEntity<String> actualResult = userService.debitUserAccount(currentUser, "200", methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("bankBalanceInsufficient");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testDebitUserAccountThrowAnException() {
    // Arrange
    doThrow(new RuntimeException("Message d'erreur")).when(userRepositoryMock).updateBankBalance(any(int.class),
        any(BigDecimal.class));

    // Act
    ResponseEntity<String> actualResult = userService.debitUserAccount(currentUser, "20", methodName);

    // Assert
    ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.OK).body("Exception");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void testGetCurrentUserWithOAuth2User() {
    // Arrange
    User expectedUser = new User(0, "Quentin", "Beraud", new BigDecimal("0"), userMail, null);
    SecurityContextHolder.getContext().setAuthentication(authenticationMock);
    when(authenticationMock.getPrincipal()).thenReturn(setAuthenticationOAuth2User());
    when(userRepositoryMock.save(any(User.class))).thenReturn(expectedUser);

    // Act
    User resultUser = userService.getCurrentUser();

    // Assert
    assertEquals(expectedUser, resultUser);
    verify(bankAccountServiceMock, times(1)).addBankAccount(any(BankAccount.class));
  }

  @Test
  void testGetCurrentUserWithOAuth2UserExisting() {
    // Arrange
    SecurityContextHolder.getContext().setAuthentication(authenticationMock);
    when(authenticationMock.getPrincipal()).thenReturn(setAuthenticationOAuth2User());
    when(userService.getUserByMail(userMail)).thenReturn(currentUser);

    // Act
    User resultUser = userService.getCurrentUser();

    // Assert
    assertEquals(currentUser, resultUser);
    verify(bankAccountServiceMock, times(0)).addBankAccount(any(BankAccount.class));
  }

  @Test
  void testGetCurrentUserWithNullAuth2UserMail() {
    // Arrange
    // OAuth2User Authentication with null email :
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", null);
    attributes.put("given_name", "Quentin");
    attributes.put("family_name", "Beraud");
    Collection<? extends GrantedAuthority> authorities = Collections
        .singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    OAuth2User oauth2User = new DefaultOAuth2User(
        authorities,
        attributes,
        "email");
    SecurityContextHolder.getContext().setAuthentication(authenticationMock);
    when(authenticationMock.getPrincipal()).thenReturn(oauth2User);

    // Act
    User resultUser = userService.getCurrentUser();

    // Assert
    assertNull(resultUser);
    verify(bankAccountServiceMock, never()).addBankAccount(any(BankAccount.class));
  }

  @Test
  void testGetCurrentUserWithInstanceOfNothing() {
    // Arrange
    SecurityContextHolder.getContext().setAuthentication(authenticationMock);
    when(authenticationMock.getPrincipal()).thenReturn(null);

    // Act
    User resultUser = userService.getCurrentUser();

    // Assert
    assertNull(resultUser);
    verify(bankAccountServiceMock, never()).addBankAccount(any(BankAccount.class));
  }

  @Test
  void testGetListUserById() {
    // Arrange
    UserRepository userRepositoryMock = mock(UserRepository.class);
    UserService userService = new UserService();
    userService.userRepository = userRepositoryMock;
    List<Integer> contactIds = Arrays.asList(1, 2, 3);
    List<User> expectedUsers = Arrays.asList(
        new User(1, "User1", null, null, null, null),
        new User(2, "User2", null, null, null, null),
        new User(3, "User3", null, null, null, null));
    when(userRepositoryMock.findAllByIdIn(contactIds)).thenReturn(expectedUsers);

    // Act
    List<User> actualUsers = userService.getListUserById(contactIds);

    // Assert
    assertEquals(expectedUsers, actualUsers);
    verify(userRepositoryMock, times(1)).findAllByIdIn(contactIds);
  }

  @Test
  void testMakeTransaction() {
    // Arrange
    Transaction t = new Transaction();
    t.setId(1);
    t.setFare(new BigDecimal("20"));
    t.setMonetizedFare(new BigDecimal("0.10"));
    t.setDescription("Descrition");
    t.setDate(new Date());
    t.setDebitUserId(1);
    t.setCreditUserId(2);

    when(userRepositoryMock.findById(t.getDebitUserId())).thenReturn(currentUser);
    when(userRepositoryMock.findById(t.getCreditUserId())).thenReturn(contactUser);

    // Act
    userService.makeTransaction(t);

    // Assert
    verify(userRepositoryMock, times(1)).updateBankBalance(1, new BigDecimal("79.90"));
    verify(userRepositoryMock, times(1)).updateBankBalance(2, new BigDecimal("120.00"));
  }
}