package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final InterestRateCalculator interestRateCalculator;

    public LoanSimulationResponse calculateLoanSimulation(LoanSimulationRequest request) {
        BigDecimal annualInterestRate = interestRateCalculator.calculateInterestRate(request);

        BigDecimal monthlyPayment = calculateMonthlyRate(request.getLoanAmount(), request.getLoanTermInMonths(), annualInterestRate);

        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(request.getLoanTermInMonths()));

        return new LoanSimulationResponse(
                UUID.randomUUID().toString(),
                request.getClientId(),
                request.getLoanAmount(),
                annualInterestRate,
                request.getLoanTermInMonths(),
                totalPayment,
                monthlyPayment,
                totalPayment.subtract(request.getLoanAmount())
        );
    }

    private static BigDecimal calculateMonthlyRate(BigDecimal amount, int loanTermInMonths, BigDecimal annualInterestRate) {
        BigDecimal monthlyInterestRate = annualInterestRate
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);

        // PMT = PV * [i(1+i)^n] / [(1+i)^n - 1]
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyInterestRate);
        BigDecimal powerFactor = onePlusRate.pow(loanTermInMonths);

        return amount
                .multiply(monthlyInterestRate)
                .multiply(powerFactor)
                .divide(powerFactor.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP);
    }
}
