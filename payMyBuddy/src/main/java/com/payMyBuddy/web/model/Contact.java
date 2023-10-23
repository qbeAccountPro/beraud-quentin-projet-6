package com.paymybuddy.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "Contact")
public class Contact {
  @Id
  @Column(name = "user_1_Id")
  private int user_1_id;

  @Id
  @Column(name = "user_2_Id")
  private int user_2_id;
}