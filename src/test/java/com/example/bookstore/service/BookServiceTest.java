package com.example.bookstore.service;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.DuplicateResourceException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.impl.BookServiceImpl;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookServiceImpl bookService;

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
    @DisplayName("Should return all books")
    void shouldReturnAllBooks() {
        List<Book> expectedBooks = Arrays.asList(testBook, testBook2);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<BookResponse> result = bookService.getAllBooks();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("book title 1");
        assertThat(result.get(1).getTitle()).isEqualTo("book title 2");
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Should return book when ID exists")
    void shouldReturnBookWhenIdExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        BookResponse result = bookService.getBookById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("book title 1");
        assertThat(result.getId()).isEqualTo(1L);
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Book not found with id: 1");
    }

    @Test
    @DisplayName("Should return book when ISBN exists")
    void shouldReturnBookWhenIsbnExists() {
        when(bookRepository.findByIsbn("123456")).thenReturn(Optional.of(testBook));

        BookResponse result = bookService.getBookByIsbn("123456");

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("123456");
        verify(bookRepository).findByIsbn("123456");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ISBN does not exist")
    void shouldThrowResourceNotFoundExceptionWhenIsbnDoesNotExist() {
        when(bookRepository.findByIsbn("999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookByIsbn("999999"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Book not found with ISBN: 999999");
    }

    @Test
    @DisplayName("Should return books by author")
    void shouldReturnBooksByAuthor() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByAuthor("author 1")).thenReturn(expectedBooks);

        List<BookResponse> result = bookService.getBooksByAuthor("author 1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("author 1");
        verify(bookRepository).findByAuthor("author 1");
    }

    @Test
    @DisplayName("Should return books by title search")
    void shouldReturnBooksByTitleSearch() {
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByTitleContaining("title")).thenReturn(expectedBooks);

        List<BookResponse> result = bookService.searchBooksByTitle("title");

        assertThat(result).hasSize(1);
        verify(bookRepository).findByTitleContaining("title");
    }

    @Test
    @DisplayName("Should create book when ISBN does not exist")
    void shouldCreateBookWhenIsbnDoesNotExist() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("title 1");
        bookRequest.setAuthor("author 1");
        bookRequest.setIsbn("111222");
        bookRequest.setPrice(new BigDecimal("18.50"));
        bookRequest.setQuantity(25);

        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
        when(jdbcTemplate.getJdbcOperations()).thenReturn(mock(org.springframework.jdbc.core.JdbcTemplate.class));
        when(jdbcTemplate.getJdbcOperations().queryForObject(anyString(), eq(Long.class))).thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(org.springframework.jdbc.core.namedparam.SqlParameterSource.class))).thenReturn(1);

        BookResponse result = bookService.createBook(bookRequest);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("title 1");
        assertThat(result.getIsbn()).isEqualTo("111222");
        verify(bookRepository).findByIsbn("111222");
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when creating book with duplicate ISBN")
    void shouldThrowExceptionWhenCreatingBookWithDuplicateIsbn() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setIsbn("123456");

        when(bookRepository.findByIsbn("123456")).thenReturn(Optional.of(testBook));

        assertThatThrownBy(() -> bookService.createBook(bookRequest))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should update book when ID exists")
    void shouldUpdateBookWhenIdExists() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("book title 1 updated");
        updateRequest.setAuthor("author 1");
        updateRequest.setIsbn("123456"); // Same ISBN as existing book
        updateRequest.setPrice(new BigDecimal("30.00"));
        updateRequest.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        // ISBN is not changing, so findByIsbn won't be called for conflict check
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        verify(bookRepository, never()).findByIsbn(anyString());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent book")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentBook() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(999L, updateRequest))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Book not found with id: 999");
    }

    @Test
    @DisplayName("Should patch book when ID exists")
    void shouldPatchBookWhenIdExists() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setPrice(new BigDecimal("40.25"));
        patchRequest.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.patchBook(1L, patchRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when patching non-existent book")
    void shouldThrowResourceNotFoundExceptionWhenPatchingNonExistentBook() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setPrice(new BigDecimal("50.00"));

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.patchBook(999L, patchRequest))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Book not found with id: 999");
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when patching with conflicting ISBN")
    void shouldThrowDuplicateResourceExceptionWhenPatchingWithConflictingIsbn() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setIsbn("789012"); // Conflicts with testBook2

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.findByIsbn("789012")).thenReturn(Optional.of(testBook2));

        assertThatThrownBy(() -> bookService.patchBook(1L, patchRequest))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should delete book when ID exists")
    void shouldDeleteBookWhenIdExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent book")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentBook() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Book not found with id: 999");
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when updating with conflicting ISBN")
    void shouldThrowDuplicateResourceExceptionWhenUpdatingWithConflictingIsbn() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Author");
        updateRequest.setIsbn("999999"); // Different from existing
        updateRequest.setPrice(new BigDecimal("29.99"));
        updateRequest.setQuantity(10);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.findByIsbn("999999")).thenReturn(Optional.of(testBook2));

        assertThatThrownBy(() -> bookService.updateBook(1L, updateRequest))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should update book when ISBN is not changing")
    void shouldUpdateBookWhenIsbnIsNotChanging() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setIsbn("123456"); // Same as existing book's ISBN
        updateRequest.setPrice(new BigDecimal("30.00"));
        updateRequest.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        // findByIsbn is only called when ISBN is different, so it shouldn't be called here
        verify(bookRepository, never()).findByIsbn(anyString());
    }

    @Test
    @DisplayName("Should return empty list when no books by author")
    void shouldReturnEmptyListWhenNoBooksByAuthor() {
        when(bookRepository.findByAuthor("Unknown Author")).thenReturn(Collections.emptyList());

        List<BookResponse> result = bookService.getBooksByAuthor("Unknown Author");

        assertThat(result).isEmpty();
        verify(bookRepository).findByAuthor("Unknown Author");
    }

    @Test
    @DisplayName("Should return empty list when no books by title search")
    void shouldReturnEmptyListWhenNoBooksByTitleSearch() {
        when(bookRepository.findByTitleContaining("nonexistent")).thenReturn(Collections.emptyList());

        List<BookResponse> result = bookService.searchBooksByTitle("nonexistent");

        assertThat(result).isEmpty();
        verify(bookRepository).findByTitleContaining("nonexistent");
    }

    @Test
    @DisplayName("Should patch book with no fields updated")
    void shouldPatchBookWithNoFieldsUpdated() {
        BookUpdateRequest emptyRequest = new BookUpdateRequest();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.patchBook(1L, emptyRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should patch book when ISBN is not changing")
    void shouldPatchBookWhenIsbnIsNotChanging() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setIsbn("123456"); // Same as existing
        patchRequest.setPrice(new BigDecimal("40.00"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.patchBook(1L, patchRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should create book when ISBN is null")
    void shouldCreateBookWhenIsbnIsNull() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("Test Book");
        bookRequest.setAuthor("Test Author");
        bookRequest.setIsbn(null);
        bookRequest.setPrice(new BigDecimal("19.99"));
        bookRequest.setQuantity(10);

        // When ISBN is null, findByIsbn is not called in the service
        when(jdbcTemplate.getJdbcOperations()).thenReturn(mock(org.springframework.jdbc.core.JdbcTemplate.class));
        when(jdbcTemplate.getJdbcOperations().queryForObject(anyString(), eq(Long.class))).thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(org.springframework.jdbc.core.namedparam.SqlParameterSource.class))).thenReturn(1);

        BookResponse result = bookService.createBook(bookRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(bookRepository, never()).findByIsbn(anyString());
    }

    @Test
    @DisplayName("Should update all fields in patch request")
    void shouldUpdateAllFieldsInPatchRequest() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setTitle("New Title");
        patchRequest.setAuthor("New Author");
        patchRequest.setIsbn("999888");
        patchRequest.setPrice(new BigDecimal("99.99"));
        patchRequest.setQuantity(999);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.findByIsbn("999888")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.patchBook(1L, patchRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should update book when ISBN is same as existing (not changing)")
    void shouldUpdateBookWhenIsbnIsSameAsExisting() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setIsbn("123456"); // Same as testBook's ISBN
        updateRequest.setPrice(new BigDecimal("30.00"));
        updateRequest.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        // When ISBN is same, findByIsbn should not be called for conflict check
        verify(bookRepository, never()).findByIsbn(anyString());
    }

    @Test
    @DisplayName("Should patch book when ISBN is same as existing (not changing)")
    void shouldPatchBookWhenIsbnIsSameAsExisting() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setIsbn("123456"); // Same as testBook's ISBN
        patchRequest.setPrice(new BigDecimal("50.00"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.patchBook(1L, patchRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        // When ISBN is same, findByIsbn should not be called for conflict check
        verify(bookRepository, never()).findByIsbn(anyString());
    }

    @Test
    @DisplayName("Should update book when new ISBN is different and available")
    void shouldUpdateBookWhenNewIsbnIsDifferentAndAvailable() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setIsbn("999888"); // Different from testBook's ISBN
        updateRequest.setPrice(new BigDecimal("30.00"));
        updateRequest.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.findByIsbn("999888")).thenReturn(Optional.empty()); // Available
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).findByIsbn("999888"); // Should check for conflict
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should patch book when new ISBN is different and available")
    void shouldPatchBookWhenNewIsbnIsDifferentAndAvailable() {
        BookUpdateRequest patchRequest = new BookUpdateRequest();
        patchRequest.setIsbn("999888"); // Different from testBook's ISBN
        patchRequest.setPrice(new BigDecimal("50.00"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.findByIsbn("999888")).thenReturn(Optional.empty()); // Available
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.patchBook(1L, patchRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).findByIsbn("999888"); // Should check for conflict
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should update book when ISBN is null in request")
    void shouldUpdateBookWhenIsbnIsNullInRequest() {
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setIsbn(null);
        updateRequest.setPrice(new BigDecimal("30.00"));
        updateRequest.setQuantity(100);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        // When ISBN is null, conflict check should not be performed
        verify(bookRepository, never()).findByIsbn(anyString());
        verify(bookRepository).save(any(Book.class));
    }
}
