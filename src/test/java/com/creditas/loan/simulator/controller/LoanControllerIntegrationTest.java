package com.creditas.loan.simulator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("LoanController Integration Tests")
public class LoanControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Should process loan simulation successfully with real service layer")
  public void shouldProcessLoanSimulationSuccessfully() throws Exception {

    LoanSimulationRequest request = new LoanSimulationRequest(
        "123e4567-e89b-12d3-a456-426614174000",
        new BigDecimal("10000.00"),
        LocalDate.of(1990, 12, 12),
        12
    );

    mockMvc.perform(post("/v1/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.client_id").value("123e4567-e89b-12d3-a456-426614174000"))
            .andExpect(jsonPath("$.loan_amount").value(10000.00))
            .andExpect(jsonPath("$.loan_term_in_months").value(12))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.interest_rate").exists())
            .andExpect(jsonPath("$.total_amount_payable").exists())
            .andExpect(jsonPath("$.monthly_installment").exists())
            .andExpect(jsonPath("$.total_interest_paid").exists());
  }

  @Test
  @DisplayName("Should return bad request for invalid loan amount")
  public void shouldReturnBadRequestForInvalidLoanAmount() throws Exception {

    LoanSimulationRequest request = new LoanSimulationRequest(
        "123e4567-e89b-12d3-a456-426614174000",
        new BigDecimal("-1000.00"), // Invalid negative amount
        LocalDate.of(1990, 12, 12),
        12
    );

    mockMvc.perform(post("/v1/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return bad request for invalid birth date")
  public void shouldReturnBadRequestForInvalidBirthDate() throws Exception {

    LoanSimulationRequest request = new LoanSimulationRequest(
        "123e4567-e89b-12d3-a456-426614174000",
        new BigDecimal("10000.00"),
        LocalDate.now().plusDays(1), // Future birth date
        12
    );

    mockMvc.perform(post("/v1/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

  }

  @Test
  @DisplayName("Should return bad request for invalid loan term")
  public void shouldReturnBadRequestForInvalidLoanTerm() throws Exception {

    LoanSimulationRequest request = new LoanSimulationRequest(
        "123e4567-e89b-12d3-a456-426614174000",
        new BigDecimal("10000.00"),
        LocalDate.of(1990, 12, 12),
        0 // Invalid loan term
    );

    mockMvc.perform(post("/v1/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
}
