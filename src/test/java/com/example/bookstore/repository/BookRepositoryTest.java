package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:sqlite::memory:",
    "spring.datasource.driver-class-name=org.sqlite.JDBC",
    "spring.sql.init.mode=never"
})
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Create table for in-memory SQLite
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                isbn TEXT UNIQUE,
                price DECIMAL(10, 2),
                quantity INTEGER DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
    }

    @Test
    void shouldSaveAndRetrieveBook() {
        Book book = new Book();
        book.setTitle("book title 1");
        book.setAuthor("author 1");
        book.setIsbn("123456");
        book.setPrice(new BigDecimal("25.50"));
        book.setQuantity(10);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        jdbcTemplate.update(
            "INSERT INTO books (title, author, isbn, price, quantity, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPrice(),
            book.getQuantity(), book.getCreatedAt(), book.getUpdatedAt()
        );
        Long id = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long.class);
        book.setId(id);

        Optional<Book> found = bookRepository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("book title 1");
        assertThat(found.get().getIsbn()).isEqualTo("123456");
    }

    @Test
    void shouldFindAllBooks() {
        insertTestBook("book title 1", "author 1", "123456", new BigDecimal("25.50"), 5);
        insertTestBook("book title 2", "author 2", "789012", new BigDecimal("33.75"), 12);

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(2);
    }

    @Test
    void shouldFindBookByIsbn() {
        Long id = insertTestBook("title 1", "author 1", "456789", 
            new BigDecimal("18.00"), 20);

        Optional<Book> found = bookRepository.findByIsbn("456789");

        assertThat(found).isPresent();
        assertThat(found.get().getIsbn()).isEqualTo("456789");
    }

    @Test
    void shouldFindBooksByAuthor() {
        insertTestBook("book title 1", "author 1", "111222", new BigDecimal("12.00"), 5);
        insertTestBook("book title 2", "author 1", "333444", new BigDecimal("22.50"), 10);
        insertTestBook("title 3", "author 2", "555666", new BigDecimal("15.75"), 15);

        List<Book> books = bookRepository.findByAuthor("author 1");

        assertThat(books).hasSize(2);
    }

    @Test
    void shouldFindBooksByTitleContaining() {
        insertTestBook("book title 1", "author 1", "123456", 
            new BigDecimal("25.50"), 50);
        insertTestBook("book title 2", "author 2", "789012", 
            new BigDecimal("33.75"), 30);
        insertTestBook("title 3", "author 3", "345678", 
            new BigDecimal("12.00"), 20);

        List<Book> books = bookRepository.findByTitleContaining("book");

        assertThat(books).hasSize(2);
    }

    @Test
    void shouldDeleteBookById() {
        Long id = insertTestBook("book title 1", "author 1", "123456", 
            new BigDecimal("25.50"), 5);

        bookRepository.deleteById(id);

        assertThat(bookRepository.findById(id)).isEmpty();
    }

    // Helper method to insert test data
    private Long insertTestBook(String title, String author, String isbn, 
                                BigDecimal price, Integer quantity) {
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(
            "INSERT INTO books (title, author, isbn, price, quantity, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            title, author, isbn, price, quantity, now, now
        );
        return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long.class);
    }
}

