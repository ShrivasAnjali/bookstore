package com.example.bookstore.controller;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.dto.ErrorResponse;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for book operations.
 * Handles HTTP requests and responses for book management.
 * 
 * This controller provides comprehensive CRUD operations for managing books in the bookstore,
 * including creation, retrieval, updating, and deletion of book records. It also supports
 * searching and filtering operations by various criteria such as ISBN, author, and title.
 */
@RestController
@RequestMapping("/api/books")
@Validated
@Tag(name = "Books", description = "Book Management API - Provides endpoints for creating, retrieving, updating, and deleting books in the bookstore. Supports search and filtering by ISBN, author, and title.")
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
    @Operation(
            summary = "Get all books", 
            description = "Retrieves a complete list of all books currently available in the bookstore. Returns an empty list if no books exist."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Successfully retrieved list of books",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}]")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
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
    @Operation(
            summary = "Get book by ID", 
            description = "Retrieves detailed information about a specific book using its unique identifier. The ID is a numeric value assigned when the book is created."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Book found and returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book ID format",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Book with the specified ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":404,\"error\":\"Resource Not Found\",\"message\":\"Book with ID 123 not found\",\"path\":\"/api/books/123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(
                    description = "Unique identifier of the book to retrieve", 
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
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
    @Operation(
            summary = "Get book by ISBN", 
            description = "Retrieves detailed information about a specific book using its International Standard Book Number (ISBN). ISBNs are unique identifiers for books and can be in various formats (e.g., 978-0-7432-7356-5 or 0743273567)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Book found and returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ISBN format - ISBN cannot be blank",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Book with the specified ISBN was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":404,\"error\":\"Resource Not Found\",\"message\":\"Book with ISBN 978-0-7432-7356-5 not found\",\"path\":\"/api/books/isbn/978-0-7432-7356-5\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<BookResponse> getBookByIsbn(
            @Parameter(
                    description = "International Standard Book Number (ISBN) of the book to retrieve", 
                    required = true,
                    example = "978-0-7432-7356-5",
                    schema = @Schema(type = "string", maxLength = 20)
            )
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
    @Operation(
            summary = "Get books by author", 
            description = "Retrieves all books written by a specific author. The search is case-sensitive and matches the exact author name. Returns an empty list if no books are found for the specified author."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Successfully retrieved list of books by the author",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}]")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid author name - author cannot be blank",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(
            @Parameter(
                    description = "Name of the author to search for", 
                    required = true,
                    example = "F. Scott Fitzgerald",
                    schema = @Schema(type = "string", maxLength = 200)
            )
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
    @Operation(
            summary = "Search books by title", 
            description = "Searches for books whose titles contain the specified search term. The search is case-sensitive and performs a partial match. Returns all books where the title contains the search term as a substring. Returns an empty list if no matches are found."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Successfully retrieved list of matching books",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}]")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search term - title parameter cannot be blank",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<BookResponse>> searchBooksByTitle(
            @Parameter(
                    description = "Search term to match against book titles (partial match, case-sensitive)", 
                    required = true,
                    example = "Gatsby",
                    schema = @Schema(type = "string")
            )
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
    @Operation(
            summary = "Create a new book", 
            description = "Creates a new book entry in the bookstore. All fields are required. The ISBN must be unique - attempting to create a book with an existing ISBN will result in a conflict error. The system automatically assigns an ID and timestamps upon creation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", 
                    description = "Book successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T10:30:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Invalid input or validation failed. Check the request body for missing required fields, invalid data types, or constraint violations.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":400,\"error\":\"Validation Failed\",\"message\":\"Request validation failed\",\"errors\":{\"title\":\"Title is required\",\"price\":\"Price must be positive\"},\"path\":\"/api/books\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", 
                    description = "Conflict - A book with the specified ISBN already exists in the system",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":409,\"error\":\"Duplicate Resource\",\"message\":\"Book with ISBN 978-0-7432-7356-5 already exists\",\"path\":\"/api/books\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<BookResponse> createBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Book details to create. All fields are required.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookRequest.class),
                            examples = @ExampleObject(value = "{\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":12.99,\"quantity\":50}")
                    )
            )
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
    @Operation(
            summary = "Update a book (full update)", 
            description = "Performs a full replacement update of an existing book. All fields in the request body are required and will replace the existing values. This is a PUT operation that replaces the entire resource. For partial updates, use the PATCH endpoint instead."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Book successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":15.99,\"quantity\":75,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T11:45:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Invalid input or validation failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":400,\"error\":\"Validation Failed\",\"message\":\"Request validation failed\",\"errors\":{\"price\":\"Price must be positive\"},\"path\":\"/api/books/1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Book with the specified ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":404,\"error\":\"Resource Not Found\",\"message\":\"Book with ID 123 not found\",\"path\":\"/api/books/123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", 
                    description = "Conflict - The specified ISBN is already assigned to another book",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":409,\"error\":\"Duplicate Resource\",\"message\":\"Book with ISBN 978-0-7432-7356-5 already exists\",\"path\":\"/api/books/1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(
                    description = "Unique identifier of the book to update", 
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Complete book details to replace existing values. All fields are required.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookRequest.class),
                            examples = @ExampleObject(value = "{\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":15.99,\"quantity\":75}")
                    )
            )
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
    @Operation(
            summary = "Partially update a book", 
            description = "Performs a partial update of an existing book. Only the fields provided in the request body will be updated; all other fields remain unchanged. This is useful when you only need to update specific properties like price or quantity without modifying other fields."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Book successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"The Great Gatsby\",\"author\":\"F. Scott Fitzgerald\",\"isbn\":\"978-0-7432-7356-5\",\"price\":15.99,\"quantity\":75,\"createdAt\":\"2024-01-15T10:30:00\",\"updatedAt\":\"2024-01-15T11:45:00\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Invalid input or validation failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":400,\"error\":\"Validation Failed\",\"message\":\"Request validation failed\",\"errors\":{\"price\":\"Price must be positive\"},\"path\":\"/api/books/1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Book with the specified ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":404,\"error\":\"Resource Not Found\",\"message\":\"Book with ID 123 not found\",\"path\":\"/api/books/123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", 
                    description = "Conflict - The specified ISBN is already assigned to another book",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":409,\"error\":\"Duplicate Resource\",\"message\":\"Book with ISBN 978-0-7432-7356-5 already exists\",\"path\":\"/api/books/1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<BookResponse> patchBook(
            @Parameter(
                    description = "Unique identifier of the book to update", 
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Partial book update details. Only include fields that need to be updated. All fields are optional.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookUpdateRequest.class),
                            examples = @ExampleObject(value = "{\"price\":15.99,\"quantity\":75}")
                    )
            )
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
    @Operation(
            summary = "Delete a book", 
            description = "Permanently deletes a book from the bookstore by its unique identifier. This operation cannot be undone. The book and all its associated data will be removed from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", 
                    description = "Book successfully deleted. No content is returned."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book ID format",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Book with the specified ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-15T10:30:00\",\"status\":404,\"error\":\"Resource Not Found\",\"message\":\"Book with ID 123 not found\",\"path\":\"/api/books/123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(
                    description = "Unique identifier of the book to delete", 
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
