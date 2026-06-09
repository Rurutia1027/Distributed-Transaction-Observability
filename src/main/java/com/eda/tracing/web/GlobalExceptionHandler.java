package com.eda.tracing.web;

import com.eda.tracing.exception.OrderNotFoundException;
import com.eda.tracing.exception.TransactionConflictException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("detail", ex.getMessage()));
    }

    @ExceptionHandler(TransactionConflictException.class)
    public ResponseEntity<Map<String, String>> conflict(TransactionConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("detail", ex.getMessage()));
    }
}
