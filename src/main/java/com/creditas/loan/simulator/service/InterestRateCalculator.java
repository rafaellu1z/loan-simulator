package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;

import java.math.BigDecimal;

public interface InterestRateCalculator {
    BigDecimal calculateInterestRate(LoanSimulationRequest request);
}
