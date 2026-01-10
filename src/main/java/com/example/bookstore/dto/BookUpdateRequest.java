package com.example.bookstore.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO for partial book update requests (PATCH operations).
 * All fields are optional for partial updates.
 */
public class BookUpdateRequest {
    
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;
    
    @Size(max = 200, message = "Author must not exceed 200 characters")
    private String author;
    
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;
    
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    /**
     * Default constructor.
     */
    public BookUpdateRequest() {
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
