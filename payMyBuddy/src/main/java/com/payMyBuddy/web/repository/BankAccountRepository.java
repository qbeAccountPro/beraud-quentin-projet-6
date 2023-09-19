package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.BankAccount;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer>{
  
}
