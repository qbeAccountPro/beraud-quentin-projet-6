package com.paymybuddy.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.repository.BankAccountRepository;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

  @Mock
  BankAccountRepository bankAccountRepositoryMock;

  @InjectMocks
  BankAccountService bankAccountService = new BankAccountService();

  @Test
  void testGetBankAccountById() {
    Integer accountId = 1;
    BankAccount mockBankAccount = new BankAccount(); 
    when(bankAccountRepositoryMock.findById(accountId)).thenReturn(Optional.of(mockBankAccount));

    Optional<BankAccount> result = bankAccountService.getBankAccountById(accountId);

    assertTrue(result.isPresent()); 
    assertEquals(mockBankAccount, result.get()); 
    
    verify(bankAccountRepositoryMock, times(1)).findById(accountId);
  }
}
