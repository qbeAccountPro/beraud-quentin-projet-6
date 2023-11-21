package com.paymybuddy.web.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePage {
  private BigDecimal balance;
  private String bankName;
  private int iBAN;
}