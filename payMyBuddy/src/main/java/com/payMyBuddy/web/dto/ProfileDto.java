package com.paymybuddy.web.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Some javadoc :
 * 
 * This class represents the Profil entity.
 * It is a data profil page object that contains only the necessary information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
  private BigDecimal balance;
  private String bankName;
  private int iBAN;
}