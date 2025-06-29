package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgeBasedInterestRateCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "20, 5.00",
            "25, 5.00",
    })
    public void shouldCalculateInterestRateForAgeBelowOrEquals25(int age, BigDecimal expectedRate) {
        AgeBasedInterestRateCalculator calculator = new AgeBasedInterestRateCalculator();

        LoanSimulationRequest request = new LoanSimulationRequest();
        request.setClientBirthDate(LocalDate.now().minusYears(age));

        BigDecimal result = calculator.calculateInterestRate(request);

        assertEquals(expectedRate, result);
    }

    @ParameterizedTest
    @CsvSource({
            "26, 3.00",
            "40, 3.00",
    })
    public void shouldCalculateInterestRateForAgeBetween26And40(int age, BigDecimal expectedRate) {
        AgeBasedInterestRateCalculator calculator = new AgeBasedInterestRateCalculator();

        LoanSimulationRequest request = new LoanSimulationRequest();
        request.setClientBirthDate(LocalDate.now().minusYears(age));

        BigDecimal result = calculator.calculateInterestRate(request);

        assertEquals(expectedRate, result);
    }

    @ParameterizedTest
    @CsvSource({
            "41, 2.00",
            "60, 2.00",
    })
    public void shouldCalculateInterestRateForAgeBetween41And60(int age, BigDecimal expectedRate) {
        AgeBasedInterestRateCalculator calculator = new AgeBasedInterestRateCalculator();

        LoanSimulationRequest request = new LoanSimulationRequest();
        request.setClientBirthDate(LocalDate.now().minusYears(age));

        BigDecimal result = calculator.calculateInterestRate(request);

        assertEquals(expectedRate, result);
    }

    @ParameterizedTest
    @CsvSource({
            "61, 4.00",
            "80, 4.00",
    })
    public void shouldCalculateInterestRateForAgeAbove60(int age, BigDecimal expectedRate) {
        AgeBasedInterestRateCalculator calculator = new AgeBasedInterestRateCalculator();

        LoanSimulationRequest request = new LoanSimulationRequest();
        request.setClientBirthDate(LocalDate.now().minusYears(age));

        BigDecimal result = calculator.calculateInterestRate(request);

        assertEquals(expectedRate, result);
    }
}
