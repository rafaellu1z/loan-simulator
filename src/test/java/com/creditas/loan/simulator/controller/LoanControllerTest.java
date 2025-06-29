package com.creditas.loan.simulator.controller;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import com.creditas.loan.simulator.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnLoanSimulationResponse_validRequest() throws Exception {

        LoanSimulationRequest request = new LoanSimulationRequest(
                "123e4567-e89b-12d3-a456-426614174000",
                new BigDecimal("10000.00"),
                LocalDate.of(1990, 12, 12),
                12
        );

        LoanSimulationResponse mockResponse = new LoanSimulationResponse(
                "123e4567-e89b-12d3-a456-426614174001",
                "123e4567-e89b-12d3-a456-426614174000",
                new BigDecimal("10000.00"),
                new BigDecimal("5.0"),
                12,
                new BigDecimal("10500.00"),
                new BigDecimal("875.00"),
                new BigDecimal("500.00")
        );

        when(loanService.calculateLoanSimulation(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/v1/loans/simulate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.client_id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.loan_amount").value(10000.00))
                .andExpect(jsonPath("$.interest_rate").value(5.0))
                .andExpect(jsonPath("$.loan_term_in_months").value(12))
                .andExpect(jsonPath("$.total_amount_payable").value(10500.00))
                .andExpect(jsonPath("$.monthly_installment").value(875.00))
                .andExpect(jsonPath("$.total_interest_paid").value(500.00));
    }
}
