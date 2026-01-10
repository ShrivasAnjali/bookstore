package com.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for bookstore root endpoints.
 * Handles home and health check endpoints.
 */
@RestController
@Tag(name = "Bookstore", description = "Bookstore root endpoints")
public class BookstoreController {

    /**
     * Home endpoint providing welcome message.
     *
     * @return map containing welcome message and status
     */
    @GetMapping("/")
    @Operation(summary = "Home endpoint", description = "Returns welcome message and bookstore status")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Bookstore API");
        response.put("status", "running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Health check endpoint.
     *
     * @return map containing health status
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns the health status of the application")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return response;
    }
}
