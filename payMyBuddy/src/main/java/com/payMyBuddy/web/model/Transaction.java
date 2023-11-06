package com.paymybuddy.web.model;

import java.util.Date;
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
 * Some javadoc.
 * 
 * This class represents a transaction object in the system.
 * it is used to hold the properties of a transaction.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "credit_user_id")
    private int creditUserId;

    @Column(name = "debit_user_id")
    private int debitUserId;

    @Column(name = "description")
    private String description;

    @Column(name = "fare")
    private BigDecimal fare;

    @Column(name = "date")
    private Date date;
}