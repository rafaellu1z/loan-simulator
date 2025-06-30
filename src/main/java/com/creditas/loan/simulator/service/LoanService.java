package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final InterestRateCalculator interestRateCalculator;

  /**
   * Calculates the loan simulation based on the provided request.
   *
   * @param request the loan simulation request containing client ID, loan amount, interest rate,
   *                and loan term in months.
   * @return a LoanSimulationResponse containing the calculated values.
   */
  public LoanSimulationResponse calculateLoanSimulation(final LoanSimulationRequest request) {
    BigDecimal annualInterestRate = interestRateCalculator.calculateInterestRate(request);

    BigDecimal monthlyPayment =
        calculateMonthlyRate(request.getLoanAmount(), request.getLoanTermInMonths(),
            annualInterestRate);

    BigDecimal totalPayment =
        monthlyPayment.multiply(BigDecimal.valueOf(request.getLoanTermInMonths()));

    BigDecimal interestPaid;
    if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
      interestPaid = BigDecimal.ZERO;
    } else {
      interestPaid = totalPayment.subtract(request.getLoanAmount());
    }

    return new LoanSimulationResponse(
        UUID.randomUUID().toString(),
        request.getClientId(),
        request.getLoanAmount(),
        annualInterestRate,
        request.getLoanTermInMonths(),
        totalPayment,
        monthlyPayment,
        interestPaid
    );
  }

  private static BigDecimal calculateMonthlyRate(final BigDecimal amount,
                                                 final int loanTermInMonths,
                                                 final BigDecimal annualInterestRate) {
    final int scale = 10;
    if (Objects.equals(annualInterestRate, BigDecimal.ZERO)) {
      return amount.divide(BigDecimal.valueOf(loanTermInMonths), scale, RoundingMode.HALF_UP);
    }
    BigDecimal monthlyInterestRate = annualInterestRate
        .divide(new BigDecimal("100"), scale, RoundingMode.HALF_UP)
        .divide(new BigDecimal("12"), scale, RoundingMode.HALF_UP);

    // PMT = PV * [i(1+i)^n] / [(1+i)^n - 1]
    BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyInterestRate);
    BigDecimal powerFactor = onePlusRate.pow(loanTermInMonths);

    return amount
        .multiply(monthlyInterestRate)
        .multiply(powerFactor)
        .divide(powerFactor.subtract(BigDecimal.ONE), scale, RoundingMode.HALF_UP);
  }
}
