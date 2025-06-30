package com.creditas.loan.simulator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Response object for loan simulation", title = "Loan Simulation Response")
public class LoanSimulationResponse {

  @Schema(description = "Unique identifier for the loan simulation", example = "123e4567-e89b-12d3-a456-426614174000")
  private String id;

  @Schema(description = "Client identifier for the loan simulation", example = "123e4567-e89b-12d3-a456-426614178000")
  private String clientId;

  @Schema(description = "Loan amount requested", example = "10000.22", format = "decimal")
  private BigDecimal loanAmount;

  @Schema(description = "Interest rate applied to the loan", example = "5.55", format = "decimal")
  private BigDecimal interestRate;

  @Schema(description = "Loan term in months", example = "24")
  private Integer loanTermInMonths;

  @Schema(description = "Total amount payable over the loan term", example = "11000.11", format = "decimal")
  private BigDecimal totalAmountPayable;

  @Schema(description = "Monthly installment amount", example = "458.33", format = "decimal")
  private BigDecimal monthlyInstallment;

  @Schema(description = "Total interest paid over the loan term", example = "1000.33", format = "decimal")
  private BigDecimal totalInterestPaid;

  /**
   * Method to round all monetary values in the response to two decimal places.
   * This is useful for ensuring consistent formatting in financial applications.
   *
   * @return the current instance of LoanSimulationResponse with rounded values
   */
  public LoanSimulationResponse withRoundedValues() {
    this.loanAmount = this.loanAmount.setScale(2, RoundingMode.HALF_UP);
    this.interestRate = this.interestRate.setScale(2, RoundingMode.HALF_UP);
    this.totalAmountPayable = this.totalAmountPayable.setScale(2, RoundingMode.HALF_UP);
    this.monthlyInstallment = this.monthlyInstallment.setScale(2, RoundingMode.HALF_UP);
    this.totalInterestPaid = this.totalInterestPaid.setScale(2, RoundingMode.HALF_UP);
    return this;
  }
}
