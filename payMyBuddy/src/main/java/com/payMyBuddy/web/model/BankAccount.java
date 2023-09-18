package com.paymybuddy.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Some javadoc.
 * 
 * This class represents a bank acoount object in the system.
 * it is used to hold the properties of bank account.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int userId;
  private String bankName;
  private int iBAN;
}