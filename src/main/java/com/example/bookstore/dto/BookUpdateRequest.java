package com.example.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO for partial book update requests (PATCH operations).
 * All fields are optional for partial updates.
 */
@Schema(description = "Request payload for partially updating a book. All fields are optional - only provided fields will be updated.", 
        example = "{\"price\":15.99,\"quantity\":75}")
public class BookUpdateRequest {
    
    @Schema(description = "Title of the book", 
            example = "The Great Gatsby", 
            maxLength = 500)
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;
    
    @Schema(description = "Name of the book author", 
            example = "F. Scott Fitzgerald", 
            maxLength = 200)
    @Size(max = 200, message = "Author must not exceed 200 characters")
    private String author;
    
    @Schema(description = "International Standard Book Number (ISBN) - unique identifier for the book", 
            example = "978-0-7432-7356-5", 
            maxLength = 20)
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;
    
    @Schema(description = "Price of the book in the store currency", 
            example = "15.99", 
            minimum = "0.01")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @Schema(description = "Quantity of books available in stock", 
            example = "75", 
            minimum = "0")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    /**
     * Default constructor.
     * Required for JSON deserialization by Spring Framework.
     * Fields are set via setters during deserialization.
     */
    public BookUpdateRequest() {
        // Empty constructor required for framework deserialization
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
}
