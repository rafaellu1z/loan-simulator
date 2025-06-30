package com.creditas.loan.simulator.controller;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import com.creditas.loan.simulator.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/loans")
@RestController
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/simulate")
    public ResponseEntity<LoanSimulationResponse> simulateLoan(@Valid @RequestBody LoanSimulationRequest request) {
        LoanSimulationResponse response = loanService.calculateLoanSimulation(request);
        return ResponseEntity.ok(response.withRoundedValues());
    }
}
