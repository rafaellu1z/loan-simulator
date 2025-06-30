package com.creditas.loan.simulator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Request object for loan simulation", title = "Loan Simulation Request")
public class LoanSimulationRequest {

  @Schema(description = "Unique identifier for the client - Optional", example = "123e4567-e89b-12d3-a456-426614174000")
  private String clientId;

  @Schema(description = "Requested loan amount", example = "10000.22", format = "decimal")
  @NotNull(message = "Loan amount must not be null")
  @Positive(message = "Loan amount must be positive")
  @DecimalMin(value = "1.00", message = "Loan amount must be at least 1.00")
  @DecimalMax(value = "1000000.00", message = "Loan amount must not exceed 1,000,000.00")
  private BigDecimal loanAmount;

  @Schema(description = "Birth date of the client - ISO 8601 format", example = "1990-01-01")
  @Past(message = "Client birth date must be in the past")
  @NotNull(message = "Client birth date must not be null")
  private LocalDate clientBirthDate;

  @Schema(description = "Months for which the loan is requested", example = "24")
  @NotNull(message = "Loan term in months must not be null")
  @Positive(message = "Loan term in months must be positive")
  private Integer loanTermInMonths;
}
