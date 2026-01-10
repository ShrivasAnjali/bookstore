package com.example.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for book response data.
 * Used to expose book information in API responses.
 */
@Schema(description = "Book information returned by the API", 
        example = "{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}")
public class BookResponse {
    
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;
    
    @Schema(description = "Title of the book", example = "The Great Gatsby")
    private String title;
    
    @Schema(description = "Name of the book author", example = "F. Scott Fitzgerald")
    private String author;
    
    @Schema(description = "International Standard Book Number (ISBN)", example = "978-0-7432-7356-5")
    private String isbn;
    
    @Schema(description = "Price of the book in the store currency", example = "12.99")
    private BigDecimal price;
    
    @Schema(description = "Quantity of books available in stock", example = "50")
    private Integer quantity;
    
    @Schema(description = "Timestamp when the book was created in the system", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the book was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public BookResponse() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id        the book ID
     * @param title     the book title
     * @param author    the book author
     * @param isbn      the book ISBN
     * @param price     the book price
     * @param quantity  the book quantity in stock
     * @param createdAt the creation timestamp
     * @param updatedAt the last update timestamp
     */
    public BookResponse(Long id, String title, String author, String isbn, BigDecimal price, 
                       Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
