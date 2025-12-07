package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    
    List<Book> findAll();
    
    Optional<Book> findById(Long id);
    
    @Query("SELECT * FROM books WHERE isbn = :isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);
    
    @Query("SELECT * FROM books WHERE author = :author")
    List<Book> findByAuthor(@Param("author") String author);
    
    @Query("SELECT * FROM books WHERE title LIKE '%' || :title || '%'")
    List<Book> findByTitleContaining(@Param("title") String title);
    
    boolean existsById(Long id);
    
    void deleteById(Long id);
}

