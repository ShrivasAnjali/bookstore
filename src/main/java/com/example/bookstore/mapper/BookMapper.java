package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.entity.Book;

import java.time.LocalDateTime;

/**
 * Mapper class for converting between Book entity and DTOs.
 * Handles all mapping logic to separate concerns from services and controllers.
 */
public class BookMapper {

    /**
     * Maps a BookRequest DTO to a Book entity.
     *
     * @param bookRequest the book request DTO
     * @return the Book entity
     */
    public static Book toEntity(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setPrice(bookRequest.getPrice());
        book.setQuantity(bookRequest.getQuantity());
        LocalDateTime now = LocalDateTime.now();
        book.setCreatedAt(now);
        book.setUpdatedAt(now);
        return book;
    }

    /**
     * Maps a Book entity to a BookResponse DTO.
     *
     * @param book the Book entity
     * @return the BookResponse DTO
     */
    public static BookResponse toResponse(Book book) {
        if (book == null) {
            return null;
        }
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setPrice(book.getPrice());
        response.setQuantity(book.getQuantity());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }

    /**
     * Updates a Book entity with values from BookUpdateRequest.
     * Only non-null fields from the request are applied.
     *
     * @param book          the existing Book entity to update
     * @param updateRequest the update request DTO
     */
    public static void updateEntity(Book book, BookUpdateRequest updateRequest) {
        if (updateRequest == null || book == null) {
            return;
        }

        if (updateRequest.getTitle() != null) {
            book.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getAuthor() != null) {
            book.setAuthor(updateRequest.getAuthor());
        }
        if (updateRequest.getIsbn() != null) {
            book.setIsbn(updateRequest.getIsbn());
        }
        if (updateRequest.getPrice() != null) {
            book.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getQuantity() != null) {
            book.setQuantity(updateRequest.getQuantity());
        }
        book.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Updates a Book entity with values from BookRequest (full update).
     *
     * @param book        the existing Book entity to update
     * @param bookRequest the book request DTO with all fields
     */
    public static void updateEntityFromRequest(Book book, BookRequest bookRequest) {
        if (bookRequest == null || book == null) {
            return;
        }

        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setPrice(bookRequest.getPrice());
        book.setQuantity(bookRequest.getQuantity());
        book.setUpdatedAt(LocalDateTime.now());
    }
}
