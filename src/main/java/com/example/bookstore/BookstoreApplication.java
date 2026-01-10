package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Bookstore application.
 * Entry point for the bookstore service.
 */
@SpringBootApplication
public class BookstoreApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }
}
