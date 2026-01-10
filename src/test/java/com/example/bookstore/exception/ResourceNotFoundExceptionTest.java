package com.example.bookstore.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        String message = "Resource not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Resource not found";
        Throwable cause = new IllegalArgumentException("Invalid ID");
        ResourceNotFoundException ex = new ResourceNotFoundException(message, cause);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
