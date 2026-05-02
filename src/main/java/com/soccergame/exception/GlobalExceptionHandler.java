package com.soccergame.exception;

import com.soccergame.dto.SimpleResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Tratamento global de exceções.
 * Converte exceções em respostas JSON consistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<SimpleResponse> handleResourceExists(ResourceAlreadyExistsException ex) {
        log.warn("Resource already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleResponse(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SimpleResponse> handleNotFound(NotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<SimpleResponse> handleUnauthorized(UnauthorizedException ex) {
        log.warn("Unauthorized access: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleResponse(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<SimpleResponse> handleBadRequest(BadRequestException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<SimpleResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleResponse> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
        FieldError fe = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String msg = (fe != null) ? fe.getField() + " " + fe.getDefaultMessage() : "Validation error";
        log.warn("Validation error: {}", msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleResponse> handleAll(Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleResponse("Internal server error"));
    }
}
