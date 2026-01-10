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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        assertThat(updateRequest.getTitle()).isEqualTo("Test");
        BookMapper.updateEntity(null, updateRequest);
        // Verify method completes without exception
        assertThat(updateRequest.getTitle()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Should not update when both entity and updateRequest are null")
    void shouldNotUpdateWhenBothAreNull() {
        // Should not throw exception
        assertThatCode(() -> BookMapper.updateEntity(null, null))
                .doesNotThrowAnyException();
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
        assertThatCode(() -> BookMapper.updateEntityFromRequest(null, null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should not update when entity is null in updateEntityFromRequest")
    void shouldNotUpdateFromRequestWhenEntityIsNull() {
        // Should not throw exception
        assertThat(bookRequest).isNotNull();
        BookMapper.updateEntityFromRequest(null, bookRequest);
        // Verify method completes without exception and bookRequest remains unchanged
        assertThat(bookRequest.getTitle()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("Should update entity with empty BookUpdateRequest (all fields null)")
    void shouldUpdateEntityWithEmptyUpdateRequest() {
        String originalTitle = entity.getTitle();
        String originalAuthor = entity.getAuthor();
        String originalIsbn = entity.getIsbn();
        BigDecimal originalPrice = entity.getPrice();
        Integer originalQuantity = entity.getQuantity();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        // Create an empty update request (all fields null)
        BookUpdateRequest emptyRequest = new BookUpdateRequest();
        
        BookMapper.updateEntity(entity, emptyRequest);
        
        // All fields should remain unchanged
        assertThat(entity.getTitle()).isEqualTo(originalTitle);
        assertThat(entity.getAuthor()).isEqualTo(originalAuthor);
        assertThat(entity.getIsbn()).isEqualTo(originalIsbn);
        assertThat(entity.getPrice()).isEqualTo(originalPrice);
        assertThat(entity.getQuantity()).isEqualTo(originalQuantity);
        // But updatedAt should still be updated
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should map BookRequest to Entity with null ISBN")
    void shouldMapBookRequestToEntityWithNullIsbn() {
        bookRequest.setIsbn(null);
        
        Book result = BookMapper.toEntity(bookRequest);
        
        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isNull();
        assertThat(result.getTitle()).isEqualTo(bookRequest.getTitle());
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should map BookRequest to Entity with null values")
    void shouldMapBookRequestToEntityWithNullValues() {
        BookRequest nullRequest = new BookRequest();
        nullRequest.setTitle(null);
        nullRequest.setAuthor(null);
        nullRequest.setIsbn(null);
        nullRequest.setPrice(null);
        nullRequest.setQuantity(null);
        
        Book result = BookMapper.toEntity(nullRequest);
        
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isNull();
        assertThat(result.getAuthor()).isNull();
        assertThat(result.getIsbn()).isNull();
        assertThat(result.getPrice()).isNull();
        assertThat(result.getQuantity()).isNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - only ISBN")
    void shouldUpdateEntityWithOnlyIsbn() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        updateRequest.setIsbn("NEWISBN123");
        
        BookMapper.updateEntity(entity, updateRequest);
        
        assertThat(entity.getIsbn()).isEqualTo("NEWISBN123");
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - only author")
    void shouldUpdateEntityWithOnlyAuthor() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        updateRequest.setAuthor("New Author");
        
        BookMapper.updateEntity(entity, updateRequest);
        
        assertThat(entity.getAuthor()).isEqualTo("New Author");
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - only quantity")
    void shouldUpdateEntityWithOnlyQuantity() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        updateRequest.setQuantity(999);
        
        BookMapper.updateEntity(entity, updateRequest);
        
        assertThat(entity.getQuantity()).isEqualTo(999);
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - only price")
    void shouldUpdateEntityWithOnlyPrice() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        updateRequest.setPrice(new BigDecimal("99.99"));
        
        BookMapper.updateEntity(entity, updateRequest);
        
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("99.99"));
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should handle toEntity with all null fields in BookRequest")
    void shouldHandleToEntityWithAllNullFields() {
        BookRequest allNullRequest = new BookRequest();
        // All fields are null by default
        
        Book result = BookMapper.toEntity(allNullRequest);
        
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isNull();
        assertThat(result.getAuthor()).isNull();
        assertThat(result.getIsbn()).isNull();
        assertThat(result.getPrice()).isNull();
        assertThat(result.getQuantity()).isNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should handle updateEntity with updateRequest having only some null fields")
    void shouldHandleUpdateEntityWithMixedNullFields() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        String originalTitle = entity.getTitle();
        String originalIsbn = entity.getIsbn();
        
        // Set only some fields, leave others null
        updateRequest.setAuthor("New Author Only");
        updateRequest.setPrice(new BigDecimal("55.55"));
        // title and isbn remain null
        
        BookMapper.updateEntity(entity, updateRequest);
        
        // Fields set should be updated
        assertThat(entity.getAuthor()).isEqualTo("New Author Only");
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("55.55"));
        // Null fields should remain unchanged
        assertThat(entity.getTitle()).isEqualTo(originalTitle);
        assertThat(entity.getIsbn()).isEqualTo(originalIsbn);
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should handle updateEntityFromRequest with null fields in BookRequest")
    void shouldHandleUpdateEntityFromRequestWithNullFields() {
        LocalDateTime originalCreatedAt = entity.getCreatedAt();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        BookRequest requestWithNulls = new BookRequest();
        requestWithNulls.setTitle(null);
        requestWithNulls.setAuthor(null);
        requestWithNulls.setIsbn(null);
        requestWithNulls.setPrice(null);
        requestWithNulls.setQuantity(null);
        
        BookMapper.updateEntityFromRequest(entity, requestWithNulls);
        
        // All fields should be set to null (full update, not partial)
        assertThat(entity.getTitle()).isNull();
        assertThat(entity.getAuthor()).isNull();
        assertThat(entity.getIsbn()).isNull();
        assertThat(entity.getPrice()).isNull();
        assertThat(entity.getQuantity()).isNull();
        assertThat(entity.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should prevent instantiation of utility class")
    void shouldPreventInstantiation() throws Exception {
        Constructor<BookMapper> constructor = BookMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        assertThatThrownBy(() -> constructor.newInstance())
            .isInstanceOf(InvocationTargetException.class)
            .hasCauseInstanceOf(UnsupportedOperationException.class)
            .hasRootCauseMessage("Utility class cannot be instantiated");
    }

    @Test
    @DisplayName("Should return null when BookRequest is null in toEntity")
    void shouldReturnNullWhenBookRequestIsNullInToEntity() {
        Book result = BookMapper.toEntity(null);
        
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should update entity with BookUpdateRequest - all null fields except one")
    void shouldUpdateEntityWithOnlyOneNonNullField() {
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        String originalTitle = entity.getTitle();
        
        // Only set one field, rest are null
        updateRequest.setPrice(new BigDecimal("88.88"));
        // title, author, isbn, quantity all null
        
        BookMapper.updateEntity(entity, updateRequest);
        
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("88.88"));
        assertThat(entity.getTitle()).isEqualTo(originalTitle); // Should remain unchanged
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
    }
}
