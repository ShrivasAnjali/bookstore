package com.example.bookstore.dto;

import java.time.LocalDateTime;

/**
 * DTO for error responses.
 * Provides a consistent structure for API error messages.
 */
public class ErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
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
