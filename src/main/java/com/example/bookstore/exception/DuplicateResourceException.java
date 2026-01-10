package com.example.bookstore.exception;

/**
 * Exception thrown when attempting to create or update a resource that conflicts with existing data.
 * Used for 409 Conflict responses (e.g., duplicate ISBN).
 */
public class DuplicateResourceException extends RuntimeException {
    
    /**
     * Constructs a new DuplicateResourceException with the specified message.
     *
     * @param message the detail message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateResourceException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
