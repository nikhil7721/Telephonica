package com.employee.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
public class CustomExceptionHandler {
	
	@RestControllerAdvice
    public static class ValidationExceptionHandler {

        @ExceptionHandler(CustomValidationException.class)
        public ResponseEntity<String> handleValidationException(CustomValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
