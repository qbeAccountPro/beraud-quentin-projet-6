package com.paymybuddy.web.model;

import java.util.Date;

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
 * This class represents a transaction object in the system.
 * it is used to hold the properties of a transaction.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "creditUserId")
    private int creditUserId;

    @Column(name = "debitUserId")
    private int debitUserId;

    @Column(name = "description")
    private String description;

    @Column(name = "fare")
    private Float fare;

    @Column(name = "date")
    private Date date;
}
