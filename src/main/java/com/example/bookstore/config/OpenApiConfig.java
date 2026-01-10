package com.example.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 * Provides API documentation for the bookstore application.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures OpenAPI documentation.
     *
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI bookstoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bookstore API")
                        .description("RESTful API for managing books in a bookstore application")
                        .version("1.0.0"));
    }
}
