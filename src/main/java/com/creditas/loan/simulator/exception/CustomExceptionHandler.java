package com.creditas.loan.simulator.exception;

import com.creditas.loan.simulator.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public final class CustomExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      final String fieldName = ((FieldError) error).getField();
      final String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    final ErrorResponse errorResponse = new ErrorResponse(
        "Validation Error",
        "One or more validation errors occurred",
        HttpStatus.BAD_REQUEST.value(),
        LocalDateTime.now(),
        errors
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonParseException(final HttpMessageNotReadableException ex) {

    String message = "Invalid JSON format";
    final Map<String, String> errors = new HashMap<>();

    final Throwable cause = ex.getCause();

    if (cause instanceof JsonParseException) {
      message = "JSON malformed";
      errors.put("json", "Malformed JSON request");
    } else if (cause instanceof InvalidFormatException formatEx) {
      message = "Invalid data format";

      if (!formatEx.getPath().isEmpty()) {
        final String fieldName = formatEx.getPath().getFirst().getFieldName();
        final String targetType = formatEx.getTargetType().getSimpleName();
        errors.put(fieldName, String.format("Value '%s' is invalid for field '%s'. Expected: %s",
            formatEx.getValue(), fieldName, getReadableTypeName(targetType)));
      } else {
        errors.put("format", "Invalid data format in JSON");
      }
    } else if (cause instanceof JsonMappingException mappingEx) {
      message = "Error mapping JSON to object";

      if (!mappingEx.getPath().isEmpty()) {
        final String fieldName = mappingEx.getPath().getFirst().getFieldName();
        errors.put(fieldName, "Error processing field: " + fieldName);
      } else {
        errors.put("mapping", "Error mapping JSON to object");
      }
    } else {
      errors.put("request", "Not possible to process the request due to malformed JSON");
    }

    final ErrorResponse errorResponse = new ErrorResponse(
        "Parse Error",
        message,
        HttpStatus.BAD_REQUEST.value(),
        LocalDateTime.now(),
        errors
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private String getReadableTypeName(final String javaTypeName) {
    return switch (javaTypeName) {
      case "BigDecimal" -> "decimal (e.g., 10000.22)";
      case "LocalDate" -> "Date (yyyy-MM-dd, e.g., 1990-01-01)";
      case "Integer" -> "integer (e.g., 24)";
      case "String" -> "uuid (e.g., 123e4567-e89b-12d3-a456-426614174000)";
      case "Boolean" -> "boolean (true/false)";
      default -> javaTypeName.toLowerCase(Locale.ROOT);
    };
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex) {

    final ErrorResponse errorResponse = new ErrorResponse(
        "Internal Server Error",
        "An unexpected error occurred",
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        LocalDateTime.now(),
        null
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
