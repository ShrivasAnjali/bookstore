package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.BookUpdateRequest;
import com.example.bookstore.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookMapper Tests")
class BookMapperTest {

    private BookRequest bookRequest;
    private Book entity;
    private BookUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        bookRequest = new BookRequest();
        bookRequest.setTitle("Test Title");
        bookRequest.setAuthor("Test Author");
        bookRequest.setIsbn("123456789");
        bookRequest.setPrice(new BigDecimal("29.99"));
        bookRequest.setQuantity(50);

        entity = new Book();
        entity.setId(1L);
        entity.setTitle("Original Title");
        entity.setAuthor("Original Author");
        entity.setIsbn("987654321");
        entity.setPrice(new BigDecimal("19.99"));
        entity.setQuantity(30);
        entity.setCreatedAt(LocalDateTime.now().minusDays(1));
        entity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        updateRequest = new BookUpdateRequest();
    }

    @Test
    @DisplayName("Should map BookRequest to Entity")
    void shouldMapBookRequestToEntity() {
        Book result = BookMapper.toEntity(bookRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(bookRequest.getTitle());
        assertThat(result.getAuthor()).isEqualTo(bookRequest.getAuthor());
        assertThat(result.getIsbn()).isEqualTo(bookRequest.getIsbn());
        assertThat(result.getPrice()).isEqualTo(bookRequest.getPrice());
        assertThat(result.getQuantity()).isEqualTo(bookRequest.getQuantity());
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result.getCreatedAt()).isEqualTo(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map Entity to BookResponse")
    void shouldMapEntityToBookResponse() {
        BookResponse result = BookMapper.toResponse(entity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getTitle()).isEqualTo(entity.getTitle());
        assertThat(result.getAuthor()).isEqualTo(entity.getAuthor());
        assertThat(result.getIsbn()).isEqualTo(entity.getIsbn());
        assertThat(result.getPrice()).isEqualTo(entity.getPrice());
        assertThat(result.getQuantity()).isEqualTo(entity.getQuantity());
        assertThat(result.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should return null when mapping null Entity to BookResponse")
    void shouldReturnNullWhenMappingNullEntity() {
        BookResponse result = BookMapper.toResponse(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - all fields")
    void shouldUpdateEntityWithAllFields() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setIsbn("111222333");
        updateRequest.setPrice(new BigDecimal("39.99"));
        updateRequest.setQuantity(100);

        BookMapper.updateEntity(entity, updateRequest);

        assertThat(entity.getTitle()).isEqualTo("Updated Title");
        assertThat(entity.getAuthor()).isEqualTo("Updated Author");
        assertThat(entity.getIsbn()).isEqualTo("111222333");
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("39.99"));
        assertThat(entity.getQuantity()).isEqualTo(100);
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - partial fields")
    void shouldUpdateEntityWithPartialFields() {
        String originalTitle = entity.getTitle();
        String originalAuthor = entity.getAuthor();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        updateRequest.setPrice(new BigDecimal("49.99"));
        updateRequest.setQuantity(75);

        BookMapper.updateEntity(entity, updateRequest);

        assertThat(entity.getTitle()).isEqualTo(originalTitle);
        assertThat(entity.getAuthor()).isEqualTo(originalAuthor);
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("49.99"));
        assertThat(entity.getQuantity()).isEqualTo(75);
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - single field")
    void shouldUpdateEntityWithSingleField() {
        String originalAuthor = entity.getAuthor();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        updateRequest.setTitle("New Title");

        BookMapper.updateEntity(entity, updateRequest);

        assertThat(entity.getTitle()).isEqualTo("New Title");
        assertThat(entity.getAuthor()).isEqualTo(originalAuthor); // Should remain unchanged
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should not update entity when updateRequest is null")
    void shouldNotUpdateEntityWhenUpdateRequestIsNull() {
        String originalTitle = entity.getTitle();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        BookMapper.updateEntity(entity, null);

        assertThat(entity.getTitle()).isEqualTo(originalTitle);
        assertThat(entity.getUpdatedAt()).isEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should not update entity when entity is null")
    void shouldNotUpdateEntityWhenEntityIsNull() {
        updateRequest.setTitle("Test");
        
        // Should not throw exception
        BookMapper.updateEntity(null, updateRequest);
    }

    @Test
    @DisplayName("Should not update when both entity and updateRequest are null")
    void shouldNotUpdateWhenBothAreNull() {
        // Should not throw exception
        BookMapper.updateEntity(null, null);
    }

    @Test
    @DisplayName("Should update entity from BookRequest - full update")
    void shouldUpdateEntityFromRequest() {
        LocalDateTime originalCreatedAt = entity.getCreatedAt();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        BookMapper.updateEntityFromRequest(entity, bookRequest);

        assertThat(entity.getTitle()).isEqualTo(bookRequest.getTitle());
        assertThat(entity.getAuthor()).isEqualTo(bookRequest.getAuthor());
        assertThat(entity.getIsbn()).isEqualTo(bookRequest.getIsbn());
        assertThat(entity.getPrice()).isEqualTo(bookRequest.getPrice());
        assertThat(entity.getQuantity()).isEqualTo(bookRequest.getQuantity());
        assertThat(entity.getCreatedAt()).isEqualTo(originalCreatedAt); // Should not change
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should not update entity when bookRequest is null")
    void shouldNotUpdateEntityWhenBookRequestIsNull() {
        String originalTitle = entity.getTitle();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        BookMapper.updateEntityFromRequest(entity, null);

        assertThat(entity.getTitle()).isEqualTo(originalTitle);
        assertThat(entity.getUpdatedAt()).isEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should not update when both entity and bookRequest are null")
    void shouldNotUpdateFromRequestWhenBothAreNull() {
        // Should not throw exception
        BookMapper.updateEntityFromRequest(null, null);
    }

    @Test
    @DisplayName("Should not update when entity is null in updateEntityFromRequest")
    void shouldNotUpdateFromRequestWhenEntityIsNull() {
        // Should not throw exception
        BookMapper.updateEntityFromRequest(null, bookRequest);
    }
}
