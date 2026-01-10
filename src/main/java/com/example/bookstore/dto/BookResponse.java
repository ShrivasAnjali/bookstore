package com.example.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for book response data.
 * Used to expose book information in API responses.
 */
public class BookResponse {
    
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;
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
