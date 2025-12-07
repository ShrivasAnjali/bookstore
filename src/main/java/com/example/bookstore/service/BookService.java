package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    
    public BookService(BookRepository bookRepository, NamedParameterJdbcTemplate jdbcTemplate) {
        this.bookRepository = bookRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }
    
    public Book createBook(Book book) {
        // Check if ISBN already exists
        if (book.getIsbn() != null && bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        LocalDateTime now = LocalDateTime.now();
        book.setCreatedAt(now);
        book.setUpdatedAt(now);
        
        // Use custom insert with SQLite's last_insert_rowid() to avoid getGeneratedKeys() issue
        String insertSql = "INSERT INTO books (title, author, isbn, price, quantity, created_at, updated_at) " +
                          "VALUES (:title, :author, :isbn, :price, :quantity, :createdAt, :updatedAt)";
        
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("title", book.getTitle())
            .addValue("author", book.getAuthor())
            .addValue("isbn", book.getIsbn())
            .addValue("price", book.getPrice())
            .addValue("quantity", book.getQuantity())
            .addValue("createdAt", book.getCreatedAt())
            .addValue("updatedAt", book.getUpdatedAt());
        
        jdbcTemplate.update(insertSql, parameters);
        
        // Get the generated ID using SQLite's last_insert_rowid()
        Long generatedId = jdbcTemplate.getJdbcOperations()
            .queryForObject("SELECT last_insert_rowid()", Long.class);
        
        book.setId(generatedId);
        return book;
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        
        // Check if ISBN is being changed and if it conflicts with another book
        if (bookDetails.getIsbn() != null && 
            !bookDetails.getIsbn().equals(book.getIsbn()) &&
            bookRepository.findByIsbn(bookDetails.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
        }
        
        if (bookDetails.getTitle() != null) {
            book.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getAuthor() != null) {
            book.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getIsbn() != null) {
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getPrice() != null) {
            book.setPrice(bookDetails.getPrice());
        }
        if (bookDetails.getQuantity() != null) {
            book.setQuantity(bookDetails.getQuantity());
        }
        book.setUpdatedAt(LocalDateTime.now());
        
        return bookRepository.save(book);
    }
    
    public Book patchBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        
        // Check if ISBN is being changed and if it conflicts with another book
        if (bookDetails.getIsbn() != null && 
            !bookDetails.getIsbn().equals(book.getIsbn()) &&
            bookRepository.findByIsbn(bookDetails.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
        }
        
        // Only update fields that are provided (not null)
        if (bookDetails.getTitle() != null) {
            book.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getAuthor() != null) {
            book.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getIsbn() != null) {
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getPrice() != null) {
            book.setPrice(bookDetails.getPrice());
        }
        if (bookDetails.getQuantity() != null) {
            book.setQuantity(bookDetails.getQuantity());
        }
        book.setUpdatedAt(LocalDateTime.now());
        
        return bookRepository.save(book);
    }
    
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    public boolean bookExists(Long id) {
        return bookRepository.existsById(id);
    }
}

