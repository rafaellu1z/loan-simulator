package com.creditas.loan.simulator.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoanService Tests")
public final class LoanServiceTest {

  private static final int LOAN_TERM_IN_MONTHS = 12;
  private static final int SCALE = 10;

  @Mock
  private InterestRateCalculator interestRateCalculator;

  @InjectMocks
  private LoanService loanService;

  private LoanSimulationRequest request;

  @BeforeEach
  public void setUp() {
    request = new LoanSimulationRequest(
        "123e4567-e89b-12d3-a456-426614175000",
        new BigDecimal("20000.00"),
        LocalDate.of(1990, 9, 29), // 33 years old
        LOAN_TERM_IN_MONTHS
    );
  }

  @Test
  @DisplayName("Should calculate loan simulation correctly for 12-month loan")
  public void shouldCalculateLoanSimulationCorrectlyFor12MonthLoan() {

    BigDecimal expectedAnnualInterestRate = new BigDecimal("3.00");

    when(interestRateCalculator.calculateInterestRate(any())).thenReturn(
        expectedAnnualInterestRate);

    LoanSimulationResponse response = loanService.calculateLoanSimulation(request);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isNotNull();
    assertThat(response.getClientId()).isEqualTo("123e4567-e89b-12d3-a456-426614175000");
    assertThat(response.getLoanAmount()).isEqualTo(new BigDecimal("20000.00"));
    assertThat(response.getInterestRate()).isEqualByComparingTo(new BigDecimal("3.00"));
    assertThat(response.getLoanTermInMonths()).isEqualTo(LOAN_TERM_IN_MONTHS);

    // Expected values based on the formula PMT = PV * [i(1+i)^n] / [(1+i)^n - 1]
    // Monthly interest rate = 3% / 100 / 12 = 0.0025
    // Monthly payment calculation:
    // PMT = 20000 * [0.0025(1+0.0025)^12] / [(1+0.0025)^12 - 1] = 1693.8739751698
    BigDecimal expectedMonthlyPayment = new BigDecimal("1693.8739751698");

    // Total payment = Monthly payment * 12
    BigDecimal expectedTotalPayment = expectedMonthlyPayment.multiply(BigDecimal.valueOf(LOAN_TERM_IN_MONTHS));

    // Total interest = Total payment - Loan amount
    BigDecimal expectedTotalInterest = expectedTotalPayment.subtract(request.getLoanAmount());

    assertThat(response.getMonthlyInstallment()).isEqualByComparingTo(expectedMonthlyPayment);
    assertThat(response.getTotalAmountPayable()).isEqualByComparingTo(expectedTotalPayment);
    assertThat(response.getTotalInterestPaid()).isEqualByComparingTo(expectedTotalInterest);
  }

  @Test
  @DisplayName("Should maintain precision in financial calculations")
  void shouldMaintainPrecisionInFinancialCalculations() {

    BigDecimal expectedRate = new BigDecimal("3.00");
    when(interestRateCalculator.calculateInterestRate(any())).thenReturn(expectedRate);

    LoanSimulationResponse response = loanService.calculateLoanSimulation(request);

    BigDecimal totalCalculated = response.getMonthlyInstallment()
        .multiply(BigDecimal.valueOf(request.getLoanTermInMonths()));

    BigDecimal difference = response.getTotalAmountPayable()
        .subtract(totalCalculated).abs();

    assertThat(difference).isLessThanOrEqualTo(new BigDecimal("0.01"));

    BigDecimal interestCalculated = response.getTotalAmountPayable()
        .subtract(response.getLoanAmount());

    assertThat(response.getTotalInterestPaid())
        .isEqualByComparingTo(interestCalculated);
  }

  @Test
  @DisplayName("Should handle zero interest rate correctly")
  void shouldHandleZeroInterestRateCorrectly() {

    BigDecimal zeroRate = BigDecimal.ZERO;
    when(interestRateCalculator.calculateInterestRate(request)).thenReturn(zeroRate);

    LoanSimulationResponse response = loanService.calculateLoanSimulation(request);

    assertThat(response.getTotalInterestPaid()).isEqualByComparingTo(BigDecimal.ZERO);

    assertThat(
        response.getTotalAmountPayable().setScale(2, RoundingMode.HALF_UP)).isEqualByComparingTo(
        request.getLoanAmount());

    BigDecimal expectedMonthlyPayment =
        request.getLoanAmount().divide(BigDecimal.valueOf(LOAN_TERM_IN_MONTHS), SCALE, RoundingMode.HALF_UP);

    assertThat(response.getMonthlyInstallment()).isEqualByComparingTo(expectedMonthlyPayment);
  }
}
