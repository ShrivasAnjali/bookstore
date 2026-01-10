package com.example.bookstore.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationException Tests")
class ValidationExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        String message = "Validation failed";
        ValidationException ex = new ValidationException(message);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Invalid format");
        ValidationException ex = new ValidationException(message, cause);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
