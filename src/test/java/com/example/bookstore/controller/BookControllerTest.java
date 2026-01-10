package com.example.bookstore.controller;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.exception.DuplicateResourceException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.exception.GlobalExceptionHandler;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("BookController Tests")
class BookControllerTest {

    private MockMvc mockMvc;
    private BookService bookService;
    private ObjectMapper objectMapper;
    private BookResponse testBookResponse;
    private BookResponse testBookResponse2;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        objectMapper = new ObjectMapper();
        BookController controller = new BookController(bookService);
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(exceptionHandler)
                .build();
        
        testBookResponse = new BookResponse();
        testBookResponse.setId(1L);
        testBookResponse.setTitle("book title 1");
        testBookResponse.setAuthor("author 1");
        testBookResponse.setIsbn("123456");
        testBookResponse.setPrice(new BigDecimal("25.50"));
        testBookResponse.setQuantity(50);
        testBookResponse.setCreatedAt(LocalDateTime.now());
        testBookResponse.setUpdatedAt(LocalDateTime.now());

        testBookResponse2 = new BookResponse();
        testBookResponse2.setId(2L);
        testBookResponse2.setTitle("book title 2");
        testBookResponse2.setAuthor("author 2");
        testBookResponse2.setIsbn("789012");
        testBookResponse2.setPrice(new BigDecimal("33.75"));
        testBookResponse2.setQuantity(30);
        testBookResponse2.setCreatedAt(LocalDateTime.now());
        testBookResponse2.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should get all books")
    void shouldGetAllBooks() throws Exception {
        List<BookResponse> books = Arrays.asList(testBookResponse, testBookResponse2);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("book title 1"))
            .andExpect(jsonPath("$[1].title").value("book title 2"));
        
        verify(bookService).getAllBooks();
    }

    @Test
    @DisplayName("Should get book by ID when exists")
    void shouldGetBookByIdWhenExists() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(testBookResponse);

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("book title 1"))
            .andExpect(jsonPath("$.id").value(1));
        
        verify(bookService).getBookById(1L);
    }

    @Test
    @DisplayName("Should return 404 when book ID does not exist")
    void shouldReturn404WhenBookIdDoesNotExist() throws Exception {
        when(bookService.getBookById(999L))
            .thenThrow(new ResourceNotFoundException("Book not found with id: 999"));

        mockMvc.perform(get("/api/books/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    @Test
    @DisplayName("Should get book by ISBN when exists")
    void shouldGetBookByIsbnWhenExists() throws Exception {
        when(bookService.getBookByIsbn("123456")).thenReturn(testBookResponse);

        mockMvc.perform(get("/api/books/isbn/123456"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isbn").value("123456"));
        
        verify(bookService).getBookByIsbn("123456");
    }

    @Test
    @DisplayName("Should get books by author")
    void shouldGetBooksByAuthor() throws Exception {
        List<BookResponse> books = Arrays.asList(testBookResponse);
        when(bookService.getBooksByAuthor("author 1")).thenReturn(books);

        mockMvc.perform(get("/api/books/author/author 1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].author").value("author 1"));
        
        verify(bookService).getBooksByAuthor("author 1");
    }

    @Test
    @DisplayName("Should search books by title")
    void shouldSearchBooksByTitle() throws Exception {
        List<BookResponse> books = Arrays.asList(testBookResponse);
        when(bookService.searchBooksByTitle("title")).thenReturn(books);

        mockMvc.perform(get("/api/books/search").param("title", "title"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("book title 1"));
        
        verify(bookService).searchBooksByTitle("title");
    }

    @Test
    @DisplayName("Should create book successfully")
    void shouldCreateBookSuccessfully() throws Exception {
        BookResponse createdBook = new BookResponse();
        createdBook.setId(3L);
        createdBook.setTitle("title 1");
        createdBook.setAuthor("author 1");
        createdBook.setIsbn("111222");
        createdBook.setPrice(new BigDecimal("18.50"));
        createdBook.setQuantity(25);

        BookRequest requestBook = new BookRequest();
        requestBook.setTitle("title 1");
        requestBook.setAuthor("author 1");
        requestBook.setIsbn("111222");
        requestBook.setPrice(new BigDecimal("18.50"));
        requestBook.setQuantity(25);

        when(bookService.createBook(any(BookRequest.class))).thenReturn(createdBook);
        String jsonBody = objectMapper.writeValueAsString(requestBook);
        
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("title 1"))
            .andExpect(jsonPath("$.id").value(3));
        
        verify(bookService).createBook(any(BookRequest.class));
    }

    @Test
    @DisplayName("Should return 409 when creating book with duplicate ISBN")
    void shouldReturn409WhenCreatingBookWithDuplicateIsbn() throws Exception {
        BookRequest requestBook = new BookRequest();
        requestBook.setTitle("Test Title");
        requestBook.setAuthor("Test Author");
        requestBook.setIsbn("123456");
        requestBook.setPrice(new BigDecimal("19.99"));
        requestBook.setQuantity(10);

        when(bookService.createBook(any(BookRequest.class)))
            .thenThrow(new DuplicateResourceException("Book with ISBN 123456 already exists"));

        String jsonBody = objectMapper.writeValueAsString(requestBook);
        
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.error").value("Duplicate Resource"));
    }

    @Test
    @DisplayName("Should update book successfully")
    void shouldUpdateBookSuccessfully() throws Exception {
        BookResponse updatedBook = new BookResponse();
        updatedBook.setId(1L);
        updatedBook.setTitle("book title 1 updated");
        updatedBook.setAuthor("author 1");
        updatedBook.setIsbn("123456");
        updatedBook.setPrice(new BigDecimal("30.00"));
        updatedBook.setQuantity(100);

        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("book title 1 updated");
        updateRequest.setAuthor("author 1");
        updateRequest.setIsbn("123456");
        updateRequest.setPrice(new BigDecimal("30.00"));
        updateRequest.setQuantity(100);

        when(bookService.updateBook(eq(1L), any(BookRequest.class))).thenReturn(updatedBook);
        String jsonBody = objectMapper.writeValueAsString(updateRequest);
        
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("book title 1 updated"));
        
        verify(bookService).updateBook(eq(1L), any(BookRequest.class));
    }

    @Test
    @DisplayName("Should patch book successfully")
    void shouldPatchBookSuccessfully() throws Exception {
        BookResponse patchedBook = new BookResponse();
        patchedBook.setId(1L);
        patchedBook.setTitle("book title 1");
        patchedBook.setPrice(new BigDecimal("40.25"));
        patchedBook.setQuantity(100);

        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setPrice(new BigDecimal("40.25"));
        patchRequest.setQuantity(100);

        when(bookService.patchBook(eq(1L), any(BookUpdateRequest.class))).thenReturn(patchedBook);
        String jsonBody = objectMapper.writeValueAsString(patchRequest);
        
        mockMvc.perform(patch("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price").value(40.25));
        
        verify(bookService).patchBook(eq(1L), any(BookUpdateRequest.class));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBookSuccessfully() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent());
        
        verify(bookService).deleteBook(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent book")
    void shouldReturn404WhenDeletingNonExistentBook() throws Exception {
        doThrow(new ResourceNotFoundException("Book not found with id: 999"))
            .when(bookService).deleteBook(999L);

        mockMvc.perform(delete("/api/books/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return empty list when no books found")
    void shouldReturnEmptyListWhenNoBooksFound() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when no books by author found")
    void shouldReturnEmptyListWhenNoBooksByAuthorFound() throws Exception {
        when(bookService.getBooksByAuthor("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books/author/Unknown"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when no books by title search found")
    void shouldReturnEmptyListWhenNoBooksByTitleFound() throws Exception {
        when(bookService.searchBooksByTitle("nonexistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books/search").param("title", "nonexistent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent book")
    void shouldReturn404WhenUpdatingNonExistentBook() throws Exception {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Author");
        updateRequest.setIsbn("123456");
        updateRequest.setPrice(new BigDecimal("29.99"));
        updateRequest.setQuantity(10);

        when(bookService.updateBook(eq(999L), any(BookRequest.class)))
            .thenThrow(new ResourceNotFoundException("Book not found with id: 999"));

        String jsonBody = objectMapper.writeValueAsString(updateRequest);
        
        mockMvc.perform(put("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 409 when updating with conflicting ISBN")
    void shouldReturn409WhenUpdatingWithConflictingIsbn() throws Exception {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Author");
        updateRequest.setIsbn("999999");
        updateRequest.setPrice(new BigDecimal("29.99"));
        updateRequest.setQuantity(10);

        when(bookService.updateBook(eq(1L), any(BookRequest.class)))
            .thenThrow(new DuplicateResourceException("Book with ISBN 999999 already exists"));

        String jsonBody = objectMapper.writeValueAsString(updateRequest);
        
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return 404 when patching non-existent book")
    void shouldReturn404WhenPatchingNonExistentBook() throws Exception {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setTitle("Updated Title");

        when(bookService.patchBook(eq(999L), any(BookUpdateRequest.class)))
            .thenThrow(new ResourceNotFoundException("Book not found with id: 999"));

        String jsonBody = objectMapper.writeValueAsString(patchRequest);
        
        mockMvc.perform(patch("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 400 when creating book with validation errors")
    void shouldReturn400WhenCreatingBookWithValidationErrors() throws Exception {
        BookRequest invalidRequest = new BookRequest();
        // Missing required fields - title, author, etc.

        String jsonBody = objectMapper.writeValueAsString(invalidRequest);
        
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when getting book by ISBN not found")
    void shouldReturn404WhenGettingBookByIsbnNotFound() throws Exception {
        when(bookService.getBookByIsbn("999999"))
            .thenThrow(new ResourceNotFoundException("Book not found with ISBN: 999999"));

        mockMvc.perform(get("/api/books/isbn/999999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }
}
