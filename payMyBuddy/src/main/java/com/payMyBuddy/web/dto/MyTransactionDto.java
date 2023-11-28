package com.paymybuddy.web.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Some javadoc :
 * 
 * This class represents the Transaction entity.
 * It is a data transfer object that contains only the necessary information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyTransactionDto {
  private String lastName;
  private String firstName;
  private String description;
  private BigDecimal fare;
  private BigDecimal monetizedFare;
  private Date date;
}
