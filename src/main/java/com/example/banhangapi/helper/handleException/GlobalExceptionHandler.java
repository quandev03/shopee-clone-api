package com.example.banhangapi.helper.handleException;

import com.example.banhangapi.helper.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // Phương thức xây dựng ResponseEntity
    private ResponseEntity<Object> buildResponseEntity2(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    // Xử lý các ngoại lệ tùy chỉnh
    @ExceptionHandler(DuplicateProductNameException.class)
    public ResponseEntity<Object> handleDuplicateProductNameException(DuplicateProductNameException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                "Duplicate product name found."
        );
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "User not found."
        );
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(CommentNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "Comment not found."
        );
        return buildResponseEntity(apiError);
    }

    // Xử lý các ngoại lệ chung khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getLocalizedMessage(),
                "An unexpected error occurred."
        );
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        log.error("Expired JWT token --- ", ex);
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "Token has expired",
                ex.getMessage()
        );
        return buildResponseEntity(apiError);
    }

    // Xử lý các lỗi validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
        return buildResponseEntity(apiError);
    }


    // Xử lý các lỗi vi phạm ràng buộc cơ sở dữ liệu
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {

        String error = "Database error: " + ex.getMostSpecificCause().getMessage();
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                "Data integrity violation",
                error
        );
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex, WebRequest request) {
        String error = "Database error: " + ex.getMessage();
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                "Not found product",
                error
        );
        return buildResponseEntity(apiError);
    }


    // Helper method để xây dựng ResponseEntity
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}