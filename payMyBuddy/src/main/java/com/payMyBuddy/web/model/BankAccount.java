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
@Table(name = "BankAccount")
public class BankAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "userid")
  private int userId;

  @Column(name = "bankName")
  private String bankName;

  @Column(name = "iBAN")
  private int iBAN;
}