package com.example.bookstore.controller;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for book operations.
 * Handles HTTP requests and responses for book management.
 */
@RestController
@RequestMapping("/api/books")
@Validated
@Tag(name = "Books", description = "API for managing books in the bookstore")
public class BookController {
    
    private final BookService bookService;
    
    /**
     * Constructor for dependency injection.
     *
     * @param bookService the book service
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    /**
     * Retrieves all books.
     *
     * @return list of all books
     */
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves a list of all books in the bookstore")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of books")
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    /**
     * Retrieves a book by its ID.
     *
     * @param id the book ID
     * @return the book response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a specific book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true)
            @PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
    
    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the book ISBN
     * @return the book response
     */
    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get book by ISBN", description = "Retrieves a specific book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponse> getBookByIsbn(
            @Parameter(description = "ISBN of the book to retrieve", required = true)
            @PathVariable @NotBlank String isbn) {
        BookResponse book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }
    
    /**
     * Retrieves all books by author.
     *
     * @param author the author name
     * @return list of books by the author
     */
    @GetMapping("/author/{author}")
    @Operation(summary = "Get books by author", description = "Retrieves all books written by a specific author")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of books")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(
            @Parameter(description = "Name of the author", required = true)
            @PathVariable @NotBlank String author) {
        List<BookResponse> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }
    
    /**
     * Searches books by title (partial match).
     *
     * @param title the title to search for
     * @return list of matching books
     */
    @GetMapping("/search")
    @Operation(summary = "Search books by title", description = "Searches for books with titles containing the search term")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of matching books")
    public ResponseEntity<List<BookResponse>> searchBooksByTitle(
            @Parameter(description = "Title search term", required = true)
            @RequestParam @NotBlank String title) {
        List<BookResponse> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }
    
    /**
     * Creates a new book.
     *
     * @param bookRequest the book creation request
     * @return the created book response
     */
    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book in the bookstore")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed"),
            @ApiResponse(responseCode = "409", description = "Book with this ISBN already exists")
    })
    public ResponseEntity<BookResponse> createBook(
            @Parameter(description = "Book details to create", required = true)
            @Valid @RequestBody BookRequest bookRequest) {
        BookResponse createdBook = bookService.createBook(bookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }
    
    /**
     * Updates an existing book (full update).
     *
     * @param id          the book ID
     * @param bookRequest the book update request
     * @return the updated book response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Performs a full update of an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "ISBN conflicts with another book")
    })
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "ID of the book to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated book details", required = true)
            @Valid @RequestBody BookRequest bookRequest) {
        BookResponse updatedBook = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }
    
    /**
     * Partially updates an existing book.
     *
     * @param id            the book ID
     * @param updateRequest the partial update request
     * @return the updated book response
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a book", description = "Performs a partial update of an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "ISBN conflicts with another book")
    })
    public ResponseEntity<BookResponse> patchBook(
            @Parameter(description = "ID of the book to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Partial book update details", required = true)
            @Valid @RequestBody BookUpdateRequest updateRequest) {
        BookResponse updatedBook = bookService.patchBook(id, updateRequest);
        return ResponseEntity.ok(updatedBook);
    }
    
    /**
     * Deletes a book by its ID.
     *
     * @param id the book ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book from the bookstore")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
