package com.creditas.loan.simulator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanSimulationResponse {
    private String id;
    private String clientId;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private Integer loanTermInMonths;
    private BigDecimal totalAmountPayable;
    private BigDecimal monthlyInstallment;
    private BigDecimal totalInterestPaid;

    public LoanSimulationResponse withRoundedValues() {
        this.loanAmount = this.loanAmount.setScale(2, RoundingMode.HALF_UP);
        this.interestRate = this.interestRate.setScale(2, RoundingMode.HALF_UP);
        this.totalAmountPayable = this.totalAmountPayable.setScale(2, RoundingMode.HALF_UP);
        this.monthlyInstallment = this.monthlyInstallment.setScale(2, RoundingMode.HALF_UP);
        this.totalInterestPaid = this.totalInterestPaid.setScale(2, RoundingMode.HALF_UP);
        return this;
    }
}
