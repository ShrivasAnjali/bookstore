package com.example.bookstore.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DuplicateResourceException Tests")
class DuplicateResourceExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        String message = "Duplicate resource";
        DuplicateResourceException ex = new DuplicateResourceException(message);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Duplicate resource";
        Throwable cause = new IllegalStateException("Already exists");
        DuplicateResourceException ex = new DuplicateResourceException(message, cause);
        
        assertThat(ex.getMessage()).isEqualTo(message);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
