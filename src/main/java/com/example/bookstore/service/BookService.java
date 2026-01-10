package com.example.bookstore.service;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;

import java.util.List;

/**
 * Service interface for book operations.
 * Defines the contract for business logic operations related to books.
 */
public interface BookService {
    
    /**
     * Retrieves all books.
     *
     * @return list of all books
     */
    List<BookResponse> getAllBooks();
    
    /**
     * Retrieves a book by its ID.
     *
     * @param id the book ID
     * @return the book response, or null if not found
     * @throws com.example.bookstore.exception.ResourceNotFoundException if book is not found
     */
    BookResponse getBookById(Long id);
    
    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the book ISBN
     * @return the book response, or null if not found
     * @throws com.example.bookstore.exception.ResourceNotFoundException if book is not found
     */
    BookResponse getBookByIsbn(String isbn);
    
    /**
     * Retrieves all books by author.
     *
     * @param author the author name
     * @return list of books by the author
     */
    List<BookResponse> getBooksByAuthor(String author);
    
    /**
     * Searches books by title (partial match).
     *
     * @param title the title to search for
     * @return list of matching books
     */
    List<BookResponse> searchBooksByTitle(String title);
    
    /**
     * Creates a new book.
     *
     * @param bookRequest the book creation request
     * @return the created book response
     * @throws com.example.bookstore.exception.DuplicateResourceException if ISBN already exists
     */
    BookResponse createBook(BookRequest bookRequest);
    
    /**
     * Updates an existing book (full update).
     *
     * @param id          the book ID
     * @param bookRequest the book update request
     * @return the updated book response
     * @throws com.example.bookstore.exception.ResourceNotFoundException if book is not found
     * @throws com.example.bookstore.exception.DuplicateResourceException if ISBN conflicts with another book
     */
    BookResponse updateBook(Long id, BookRequest bookRequest);
    
    /**
     * Partially updates an existing book.
     *
     * @param id             the book ID
     * @param updateRequest  the partial update request
     * @return the updated book response
     * @throws com.example.bookstore.exception.ResourceNotFoundException if book is not found
     * @throws com.example.bookstore.exception.DuplicateResourceException if ISBN conflicts with another book
     */
    BookResponse patchBook(Long id, BookUpdateRequest updateRequest);
    
    /**
     * Deletes a book by its ID.
     *
     * @param id the book ID
     * @throws com.example.bookstore.exception.ResourceNotFoundException if book is not found
     */
    void deleteBook(Long id);
}
