package com.paymybuddy.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "bankaccount")
public class BankAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "user_id")
  private int userId;

  @Column(name = "bank_name")
  private String bankName;

  @Column(name = "iban")
  private int IBAN;
}