package com.example.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for error responses.
 * Provides a consistent structure for API error messages.
 */
@Schema(description = "Standard error response structure returned when an API request fails", 
        example = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":404,\"error\":\"Resource Not Found\",\"message\":\"Book with ID 123 not found\",\"path\":\"/api/books/123\"}")
public class ErrorResponse {
    
    @Schema(description = "Timestamp when the error occurred", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP status code", example = "404")
    private int status;
    
    @Schema(description = "Error type or category", example = "Resource Not Found")
    private String error;
    
    @Schema(description = "Detailed error message describing what went wrong", example = "Book with ID 123 not found")
    private String message;
    
    @Schema(description = "API endpoint path where the error occurred", example = "/api/books/123")
    private String path;

    /**
     * Default constructor.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with status, error, and message.
     *
     * @param status  the HTTP status code
     * @param error   the error type
     * @param message the error message
     */
    public ErrorResponse(int status, String error, String message) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    /**
     * Constructor with all fields.
     *
     * @param status  the HTTP status code
     * @param error   the error type
     * @param message the error message
     * @param path    the request path
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this(status, error, message);
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
