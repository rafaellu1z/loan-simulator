package com.creditas.loan.simulator.controller;

import com.creditas.loan.simulator.dto.LoanSimulationRequest;
import com.creditas.loan.simulator.dto.LoanSimulationResponse;
import com.creditas.loan.simulator.dto.error.ErrorResponse;
import com.creditas.loan.simulator.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  /**
   * Simulates a loan based on the provided request parameters.
   *
   * @param request the loan simulation request containing the necessary parameters
   * @return a response entity containing the loan simulation response with rounded values
   */
  @PostMapping("/simulate")
  @Operation(summary = "Simulate a loan", description = "This endpoint simulates a loan based on the provided request parameters.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Loan simulation successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanSimulationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class, example = """
        {
          "error": "Internal Server Error",
          "message": "An unexpected error occurred",
          "status": 500,
          "timestamp": "2023-10-01T12:00:00Z"
        }
        """)))
  })
  public ResponseEntity<LoanSimulationResponse> simulateLoan(
      @Valid @RequestBody final LoanSimulationRequest request) {
    LoanSimulationResponse response = loanService.calculateLoanSimulation(request);
    return ResponseEntity.ok(response.withRoundedValues());
  }
}
