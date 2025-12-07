package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private Book testBook2;

    @BeforeEach
    void setUp() {
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
    void shouldReturnAllBooks() {
        List<Book> expectedBooks = Arrays.asList(testBook, testBook2);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = bookService.getAllBooks();

        assertThat(result).hasSize(2);
        verify(bookRepository).findAll();
    }

    @Test
    void shouldReturnBookWhenIdExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Optional<Book> result = bookService.getBookById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("book title 1");
    }

    @Test
    void shouldReturnBookWhenIsbnExists() {
        when(bookRepository.findByIsbn("123456")).thenReturn(Optional.of(testBook));

        Optional<Book> result = bookService.getBookByIsbn("123456");

        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo("123456");
    }

    @Test
    void shouldReturnBooksByAuthor() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByAuthor("author 1")).thenReturn(expectedBooks);

        List<Book> result = bookService.getBooksByAuthor("author 1");

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnBooksByTitleSearch() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByTitleContaining("title")).thenReturn(expectedBooks);

        List<Book> result = bookService.searchBooksByTitle("title");

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldCreateBookWhenIsbnDoesNotExist() {
        Book newBook = new Book();
        newBook.setTitle("title 1");
        newBook.setAuthor("author 1");
        newBook.setIsbn("111222");
        newBook.setPrice(new BigDecimal("18.50"));
        newBook.setQuantity(25);

        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
        when(jdbcTemplate.getJdbcOperations()).thenReturn(mock(org.springframework.jdbc.core.JdbcTemplate.class));
        when(jdbcTemplate.getJdbcOperations().queryForObject(anyString(), eq(Long.class))).thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(org.springframework.jdbc.core.namedparam.SqlParameterSource.class))).thenReturn(1);

        Book result = bookService.createBook(newBook);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("title 1");
    }

    @Test
    void shouldThrowExceptionWhenCreatingBookWithDuplicateIsbn() {
        Book newBook = new Book();
        newBook.setIsbn("123456");

        when(bookRepository.findByIsbn("123456")).thenReturn(Optional.of(testBook));

        assertThatThrownBy(() -> bookService.createBook(newBook))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    void shouldUpdateBookWhenIdExists() {
        Book updatedDetails = new Book();
        updatedDetails.setTitle("book title 1 updated");
        updatedDetails.setPrice(new BigDecimal("30.00"));
        updatedDetails.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.updateBook(1L, updatedDetails);

        assertThat(result.getTitle()).isEqualTo("book title 1 updated");
        verify(bookRepository).save(any(Book.class));
    }


    @Test
    void shouldPatchBookWhenIdExists() {
        Book patchDetails = new Book();
        patchDetails.setPrice(new BigDecimal("40.25"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.patchBook(1L, patchDetails);

        assertThat(result.getPrice()).isEqualTo(new BigDecimal("40.25"));
        assertThat(result.getTitle()).isEqualTo("book title 1");
    }

    @Test
    void shouldDeleteBookWhenIdExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }
}

