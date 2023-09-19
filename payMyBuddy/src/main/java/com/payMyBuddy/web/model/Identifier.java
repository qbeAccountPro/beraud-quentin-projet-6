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
 * This class represents a identifier object in the system.
 * it is used to hold the properties of connexion.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Identifier")
public class Identifier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "userId")
  private int userId;

  @Column(name = "mail")
  private String mail;

  @Column(name = "password")
  private String password;
}