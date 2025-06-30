package com.creditas.loan.simulator.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.creditas.loan.simulator.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public final class CustomExceptionHandlerTest {

  private final CustomExceptionHandler handler = new CustomExceptionHandler();

  @Test
  @DisplayName("Should handle MethodArgumentNotValidException and return validation errors")
  public void testHandleValidationExceptions() {

    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
    bindingResult.addError(new FieldError("request", "loanAmount", "must not be null"));

    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<ErrorResponse> response = handler.handleValidationExceptions(ex);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Validation Error");
    assertThat(response.getBody().getValidationErrors())
        .containsEntry("loanAmount", "must not be null");
  }

  @Test
  @DisplayName("Should handle InvalidFormatException and return field format error")
  public void testHandleInvalidFormatException() {
    JsonMappingException.Reference ref = new JsonMappingException.Reference(Object.class, "loanAmount");

    InvalidFormatException cause = new InvalidFormatException(null, "abc", "abc", BigDecimal.class);
    cause.prependPath(ref);

    HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid format", cause, null);

    ResponseEntity<ErrorResponse> response = handler.handleJsonParseException(ex);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Parse Error");
    assertThat(response.getBody().getValidationErrors()).containsKey("loanAmount");
  }

  @Test
  @DisplayName("Should handle unknown cause in HttpMessageNotReadableException")
  public void testHandleGenericHttpMessageNotReadableException() {
    HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Unknown error");

    ResponseEntity<ErrorResponse> response = handler.handleJsonParseException(ex);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getValidationErrors()).containsKey("request");
  }

  @Test
  @DisplayName("Should handle generic Exception and return 500 response")
  void testHandleGenericException() {
    Exception ex = new RuntimeException("Something went wrong");

    ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex);

    assertThat(response.getStatusCode().value()).isEqualTo(500);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
  }
}
