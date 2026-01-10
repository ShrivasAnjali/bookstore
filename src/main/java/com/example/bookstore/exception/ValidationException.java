package com.example.bookstore.exception;

/**
 * Exception thrown when validation fails.
 * Used for 400 Bad Request responses.
 */
public class ValidationException extends RuntimeException {
    
    /**
     * Constructs a new ValidationException with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
