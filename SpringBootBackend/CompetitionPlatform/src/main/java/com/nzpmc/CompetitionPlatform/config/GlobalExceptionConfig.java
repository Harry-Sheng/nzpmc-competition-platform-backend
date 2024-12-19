package com.nzpmc.CompetitionPlatform.config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionConfig {
    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalExceptions(Exception e) {
        return ResponseEntity.badRequest().body("An unexpected error occurred: " + e.getMessage());
    }
}
