package com.example.Cart.Infra.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(cartNotFoundException.class)
    public ResponseEntity<String> handleInvalidCartId(cartNotFoundException ex){
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(itemNotFoundException.class)
    public ResponseEntity<String> handleInvalidItemId(itemNotFoundException ex){
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(ex.getMessage());
    }

    }

