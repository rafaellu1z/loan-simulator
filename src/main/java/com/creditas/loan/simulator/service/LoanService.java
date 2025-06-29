package com.creditas.loan.simulator.service;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class LoanService {

    private InterestRateCalculator interestRateCalculator;

    public LoanSimulationResponse calculateLoanSimulation(LoanSimulationRequest request) {

        return new LoanSimulationResponse(
                UUID.randomUUID().toString(),
                request.getClientId(),
                request.getLoanAmount(),
                BigDecimal.ONE,
                request.getLoanTermInMonths(),
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );
    }
}
