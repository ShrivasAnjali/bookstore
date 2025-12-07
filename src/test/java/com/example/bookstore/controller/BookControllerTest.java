package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    private MockMvc mockMvc;
    private BookService bookService;
    private ObjectMapper objectMapper;
    private Book testBook;
    private Book testBook2;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        objectMapper = new ObjectMapper();
        BookController controller = new BookController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("book title 1");
        testBook.setAuthor("author 1");
        testBook.setIsbn("123456");
        testBook.setPrice(new BigDecimal("25.50"));
        testBook.setQuantity(50);
        testBook.setCreatedAt(LocalDateTime.now());
        testBook.setUpdatedAt(LocalDateTime.now());

        testBook2 = new Book();
        testBook2.setId(2L);
        testBook2.setTitle("book title 2");
        testBook2.setAuthor("author 2");
        testBook2.setIsbn("789012");
        testBook2.setPrice(new BigDecimal("33.75"));
        testBook2.setQuantity(30);
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(testBook, testBook2);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("book title 1"));
    }

    @Test
    void shouldGetBookByIdWhenExists() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("book title 1"));
    }

    @Test
    void shouldCreateBookSuccessfully() throws Exception {
        Book createdBook = new Book();
        createdBook.setId(3L);
        createdBook.setTitle("title 1");
        createdBook.setAuthor("author 1");
        createdBook.setIsbn("111222");
        createdBook.setPrice(new BigDecimal("18.50"));
        createdBook.setQuantity(25);

        when(bookService.createBook(any(Book.class))).thenReturn(createdBook);

        Book requestBook = new Book();
        requestBook.setTitle("title 1");
        requestBook.setAuthor("author 1");
        requestBook.setIsbn("111222");
        requestBook.setPrice(new BigDecimal("18.50"));
        requestBook.setQuantity(25);
        String jsonBody = objectMapper.writeValueAsString(requestBook);
        
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("title 1"));
    }

    @Test
    void shouldUpdateBookSuccessfully() throws Exception {
        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("book title 1 updated");
        savedBook.setPrice(new BigDecimal("30.00"));

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(savedBook);

        Book updateBook = new Book();
        updateBook.setTitle("book title 1 updated");
        updateBook.setAuthor("author 1");
        updateBook.setIsbn("123456");
        updateBook.setPrice(new BigDecimal("30.00"));
        updateBook.setQuantity(75);
        String jsonBody = objectMapper.writeValueAsString(updateBook);
        
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("book title 1 updated"));
    }

    @Test
    void shouldPatchBookSuccessfully() throws Exception {
        Book patchedBook = new Book();
        patchedBook.setId(1L);
        patchedBook.setTitle("book title 1");
        patchedBook.setPrice(new BigDecimal("40.25"));
        patchedBook.setQuantity(100);

        when(bookService.patchBook(eq(1L), any(Book.class))).thenReturn(patchedBook);

        Book patchBook = new Book();
        patchBook.setPrice(new BigDecimal("40.25"));
        patchBook.setQuantity(100);
        String jsonBody = objectMapper.writeValueAsString(patchBook);
        
        mockMvc.perform(patch("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price").value(40.25));
    }

    @Test
    void shouldDeleteBookSuccessfully() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent());
    }
}
