package com.paymybuddy.web.model;

import java.math.BigDecimal;

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
 * Some javadoc :
 * 
 * This class represents a user object in the system.
 * it is used to hold the properties of a database user.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "firstName")
  private String firstname;

  @Column(name = "lastName")
  private String lastname;

  @Column(name = "bankBalance")
  private BigDecimal bankbalance;

  @Column(name = "mail")
  private String mail;

  @Column(name = "password")
  private String password;
}