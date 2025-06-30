package com.creditas.loan.simulator.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.creditas.loan.simulator.controller.LoanController;
import com.creditas.loan.simulator.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = LoanController.class)
public final class LoanSimulationExceptionIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private LoanService loanService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Should return 400 for validation errors")
  public void testValidationError() throws Exception {
    String invalidJson = "{}";

    mockMvc.perform(post("/v1/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation Error"))
            .andExpect(jsonPath("$.validation_errors.loanTermInMonths").exists())
            .andExpect(jsonPath("$.validation_errors.clientBirthDate").exists())
            .andExpect(jsonPath("$.validation_errors.loanAmount").exists());
  }

  @Test
  @DisplayName("Should return 400 for invalid JSON format (e.g., number as string)")
  public void testInvalidFormat() throws Exception {
    String json = """
        {
          "loan_amount": "not-a-number",
          "client_birth_date": "2023-10-01",
          "loan_term_in_months": 12
        }
        """;

    mockMvc.perform(post("/v1/loans/simulate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Parse Error"))
            .andExpect(jsonPath("$.validation_errors.loan_amount").exists());
  }
}
