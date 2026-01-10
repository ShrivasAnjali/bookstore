package com.example.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 * Provides comprehensive API documentation for the bookstore application.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures OpenAPI documentation with detailed API information.
     *
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI bookstoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bookstore API")
                        .description("""
                                RESTful API for managing books in a bookstore application.
                                
                                This API provides comprehensive endpoints for:
                                - Creating, reading, updating, and deleting books
                                - Searching books by title, author, or ISBN
                                - Managing book inventory and pricing
                                
                                All endpoints follow RESTful conventions and return JSON responses.
                                Error responses follow a consistent structure for easy error handling.
                                
                                **Authentication**: Currently not required (public API)
                                **Base URL**: /api
                                **Content-Type**: application/json
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bookstore API Support")
                                .email("support@bookstore.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
