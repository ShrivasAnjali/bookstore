package com.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for bookstore root endpoints.
 * Handles home and health check endpoints.
 * 
 * This controller provides utility endpoints for the bookstore API, including
 * a welcome message endpoint and a health check endpoint for monitoring purposes.
 */
@RestController
@Tag(name = "Bookstore", description = "Bookstore Utility Endpoints - Provides root-level endpoints including welcome message and health check for monitoring and API discovery.")
public class BookstoreController {

    /**
     * Home endpoint providing welcome message.
     *
     * @return map containing welcome message and status
     */
    @GetMapping("/")
    @Operation(
            summary = "Home endpoint", 
            description = "Returns a welcome message along with the current API status and timestamp. This endpoint can be used to verify that the API is running and accessible."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Welcome message and status returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\"message\":\"Welcome to Bookstore API\",\"status\":\"running\",\"timestamp\":1705312200000}")
                    )
            )
    })
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
    @Operation(
            summary = "Health check", 
            description = "Returns the health status of the application. This endpoint is typically used by monitoring systems, load balancers, and orchestration platforms to verify that the service is operational and ready to accept requests."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Health status returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\"status\":\"UP\"}")
                    )
            )
    })
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return response;
    }
}
