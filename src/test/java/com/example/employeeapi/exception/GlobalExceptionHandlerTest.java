package com.example.employeeapi.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleEmployeeNotFound returns 404 and message")
    void handleEmployeeNotFound() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException(999L);
        ResponseEntity<java.util.Map<String, String>> response = handler.handleEmployeeNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("message", "Employee not found with id: 999");
    }

    @Test
    @DisplayName("handleDuplicateEmail returns 400 and message")
    void handleDuplicateEmail() {
        DuplicateEmailException ex = new DuplicateEmailException("dup@example.com");
        ResponseEntity<java.util.Map<String, String>> response = handler.handleDuplicateEmail(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", "Employee with email already exists: dup@example.com");
    }

    @Test
    @DisplayName("handleValidation returns 400 with field errors")
    void handleValidation() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(new FieldError("employeeRequest", "name", "Name is required")));

        ResponseEntity<java.util.Map<String, String>> response = handler.handleValidation(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("name", "Name is required");
    }
}
