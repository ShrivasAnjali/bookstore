package com.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO for book creation and update requests.
 * Contains validation annotations for request validation.
 */
public class BookRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;
    
    @NotBlank(message = "Author is required")
    @Size(max = 200, message = "Author must not exceed 200 characters")
    private String author;
    
    @NotBlank(message = "ISBN is required")
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    /**
     * Default constructor.
     */
    public BookRequest() {
    }

    /**
     * Constructor with all fields.
     *
     * @param title    the book title
     * @param author   the book author
     * @param isbn     the book ISBN
     * @param price    the book price
     * @param quantity the book quantity in stock
     */
    public BookRequest(String title, String author, String isbn, BigDecimal price, Integer quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
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
