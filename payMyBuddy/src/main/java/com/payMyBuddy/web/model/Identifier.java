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
 * This class represents a identifier object in the system.
 * it is used to hold the properties of connexion.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Identifier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int userId;
  private String mail;
  private String password;
}