package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Component
public class AgeBasedInterestRateCalculator implements InterestRateCalculator {

    @Override
    public BigDecimal calculateInterestRate(LoanSimulationRequest request) {
        int age = calculateAge(request.getClientBirthDate());
        return getInterestRate(age);
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private BigDecimal getInterestRate(int age) {
        if (age <= 25) {
            return new BigDecimal("5.00");
        } else if (age <= 40) {
            return new BigDecimal("3.00");
        } else if (age <= 60) {
            return new BigDecimal("2.00");
        } else {
            return new BigDecimal("4.00");
        }
    }
}
