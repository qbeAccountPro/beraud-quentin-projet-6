package com.paymybuddy.web.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConstantsTest {

  @Test
   void testAppMonetization() {
    // Arrange
    BigDecimal expectedMonetization = new BigDecimal("0.005");

    // Act
    BigDecimal actualMonetization = Constants.appMonetization;

    // Assert
    assertEquals(expectedMonetization, actualMonetization,
        "appMonetization constant should have the expected value");
  }
}
