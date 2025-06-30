package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;

@Component
public class AgeBasedInterestRateCalculator implements InterestRateCalculator {

  private static final int AGE_25 = 25;
  private static final int AGE_40 = 40;
  private static final int AGE_60 = 60;

  /**
   * Calculates the interest rate based on the client's age.
   *
   * @param request the loan simulation request containing the client's birthdate
   * @return the calculated interest rate as a BigDecimal
   */
  @Override
  public BigDecimal calculateInterestRate(final LoanSimulationRequest request) {
    int age = calculateAge(request.getClientBirthDate());
    return getInterestRate(age);
  }

  private int calculateAge(final LocalDate birthDate) {
    return Period.between(birthDate, LocalDate.now()).getYears();
  }

  private BigDecimal getInterestRate(final int age) {
    if (age <= AGE_25) {
      return new BigDecimal("5.00");
    } else if (age <= AGE_40) {
      return new BigDecimal("3.00");
    } else if (age <= AGE_60) {
      return new BigDecimal("2.00");
    } else {
      return new BigDecimal("4.00");
    }
  }
}
