package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.DuplicateResourceException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of BookService.
 * Contains all business logic for book operations.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {
    
    private static final String BOOK_NOT_FOUND_BY_ID_MESSAGE = "Book not found with id: %d";
    
    private final BookRepository bookRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    
    /**
     * Constructor for dependency injection.
     *
     * @param bookRepository the book repository
     * @param jdbcTemplate   the JDBC template for custom queries
     */
    public BookServiceImpl(BookRepository bookRepository, NamedParameterJdbcTemplate jdbcTemplate) {
        this.bookRepository = bookRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookMapper::toResponse)
                .toList();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(BOOK_NOT_FOUND_BY_ID_MESSAGE, id)));
        return BookMapper.toResponse(book);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Book not found with ISBN: %s", isbn)));
        return BookMapper.toResponse(book);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream()
                .map(BookMapper::toResponse)
                .toList();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContaining(title);
        return books.stream()
                .map(BookMapper::toResponse)
                .toList();
    }
    
    /**
     * Checks if an ISBN already exists and throws exception if it does.
     *
     * @param isbn the ISBN to check
     * @throws DuplicateResourceException if ISBN already exists
     */
    private void validateIsbnNotExists(String isbn) {
        if (isbn != null && bookRepository.findByIsbn(isbn).isPresent()) {
            throw new DuplicateResourceException(String.format("Book with ISBN %s already exists", isbn));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        validateIsbnNotExists(bookRequest.getIsbn());
        
        Book book = BookMapper.toEntity(bookRequest);
        
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
        return BookMapper.toResponse(book);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(BOOK_NOT_FOUND_BY_ID_MESSAGE, id)));
        
        // Check if ISBN is being changed and if it conflicts with another book
        if (bookRequest.getIsbn() != null && !bookRequest.getIsbn().equals(book.getIsbn())) {
            validateIsbnNotExists(bookRequest.getIsbn());
        }
        
        BookMapper.updateEntityFromRequest(book, bookRequest);
        Book updatedBook = bookRepository.save(book);
        return BookMapper.toResponse(updatedBook);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BookResponse patchBook(Long id, BookUpdateRequest updateRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(BOOK_NOT_FOUND_BY_ID_MESSAGE, id)));
        
        // Check if ISBN is being changed and if it conflicts with another book
        if (updateRequest.getIsbn() != null && !updateRequest.getIsbn().equals(book.getIsbn())) {
            validateIsbnNotExists(updateRequest.getIsbn());
        }
        
        BookMapper.updateEntity(book, updateRequest);
        Book updatedBook = bookRepository.save(book);
        return BookMapper.toResponse(updatedBook);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(BOOK_NOT_FOUND_BY_ID_MESSAGE, id));
        }
        bookRepository.deleteById(id);
    }
}
