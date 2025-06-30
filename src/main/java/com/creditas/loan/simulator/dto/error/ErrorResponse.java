package com.creditas.loan.simulator.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Error response object containing details about errors and other exceptions")
public final class ErrorResponse {

  @Getter
  @Schema(description = "Error type", example = "Validation Error")
  private final String error;

  @Getter
  @Schema(description = "Detailed error message", example = "One or more validation errors occurred")
  private final String message;

  @Getter
  @Schema(description = "HTTP status code", example = "400")
  private final int status;

  @Getter
  @Schema(description = "Timestamp of the error occurrence", example = "2023-10-01T12:00:00Z")
  private final LocalDateTime timestamp;

  @Schema(description = "Map of validation errors ", example = "{\"field1\": \"Error message for field1\"}")
  private final Map<String, String> validationErrors;

  public ErrorResponse(final String error,
                       final String message,
                       final int status,
                       final LocalDateTime timestamp,
                       final Map<String, String> validationErrors) {
    this.error = error;
    this.message = message;
    this.status = status;
    this.timestamp = timestamp;
    this.validationErrors = Optional.ofNullable(validationErrors)
        .map(HashMap::new)
        .orElse(null);
  }

  public Map<String, String> getValidationErrors() {
    return Optional.ofNullable(validationErrors)
        .map(Map::copyOf)
        .orElse(null);
  }
}
