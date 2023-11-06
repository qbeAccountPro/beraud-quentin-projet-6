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
 * This class represents a contact object in the system.
 * it is used to hold the properties of contact.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
public class Contact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "user_1_id")
  private int user1Id;

  @Column(name = "user_2_id")
  private int user2Id;
}