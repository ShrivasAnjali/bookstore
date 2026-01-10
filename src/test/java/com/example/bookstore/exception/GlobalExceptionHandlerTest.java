package com.example.bookstore.exception;

import com.example.bookstore.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("uri=/api/books");
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Book not found");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("Resource Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Book not found");
        assertThat(response.getBody().getPath()).isEqualTo("/api/books");
    }

    @Test
    @DisplayName("Should handle DuplicateResourceException")
    void shouldHandleDuplicateResourceException() {
        DuplicateResourceException ex = new DuplicateResourceException("ISBN already exists");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateResourceException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getError()).isEqualTo("Duplicate Resource");
        assertThat(response.getBody().getMessage()).isEqualTo("ISBN already exists");
    }

    @Test
    @DisplayName("Should handle ValidationException")
    void shouldHandleValidationException() {
        ValidationException ex = new ValidationException("Invalid input");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid input");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError1 = new FieldError("bookRequest", "title", "Title is required");
        FieldError fieldError2 = new FieldError("bookRequest", "price", "Price must be positive");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));
        
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMethodArgumentNotValidException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(400);
        assertThat(response.getBody().get("error")).isEqualTo("Validation Failed");
        assertThat(response.getBody().get("errors")).isInstanceOf(Map.class);
        
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertThat(errors).hasSize(2);
        assertThat(errors.get("title")).isEqualTo("Title is required");
        assertThat(errors.get("price")).isEqualTo("Price must be positive");
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException")
    void shouldHandleConstraintViolationException() {
        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", Collections.emptySet());
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolationException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Constraint Violation");
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Invalid Argument");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid argument");
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Unexpected error");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
    }

    @Test
    @DisplayName("Should handle exception with cause")
    void shouldHandleExceptionWithCause() {
        ResourceNotFoundException cause = new ResourceNotFoundException("Cause");
        ResourceNotFoundException ex = new ResourceNotFoundException("Book not found", cause);
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Book not found");
    }

    @Test
    @DisplayName("Should handle WebRequest with uri= prefix")
    void shouldHandleWebRequestWithUriPrefix() {
        when(webRequest.getDescription(false)).thenReturn("uri=/api/books/123");
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);
        
        assertThat(response.getBody().getPath()).isEqualTo("/api/books/123");
    }
}
