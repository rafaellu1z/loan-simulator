package com.creditas.loan.simulator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanSimulationRequest {

    @Null
    private String clientId;

    @NotNull(message = "Loan amount must not be null")
    private BigDecimal loanAmount;

    @Past(message = "Client birth date must be in the past")
    @NotNull(message = "Client birth date must not be null")
    private LocalDate clientBirthDate;

    @NotNull(message = "Loan term in months must not be null")
    @Positive(message = "Loan term in months must be positive")
    private Integer loanTermInMonths;
}
