package com.example.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.dialect.JdbcDialect;

/**
 * Database configuration.
 * Configures database-specific settings including JDBC dialect for SQLite.
 */
@Configuration
public class DatabaseConfig {
    
    /**
     * Configures the JDBC dialect for SQLite.
     *
     * @return SQLite JDBC dialect bean
     */
    @Bean
    public JdbcDialect jdbcDialect() {
        return new SqliteDialect();
    }
}

