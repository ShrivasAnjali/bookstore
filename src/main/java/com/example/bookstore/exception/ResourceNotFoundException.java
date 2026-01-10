package com.example.bookstore.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Used for 404 Not Found responses.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new ResourceNotFoundException with the specified message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
