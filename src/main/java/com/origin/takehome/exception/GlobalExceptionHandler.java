package com.origin.takehome.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            InvalidUrlException ex, WebRequest request) {
        logger.warn("Invalid URL: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid URL provided", request);
    }
    
    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            URISyntaxException ex, WebRequest request) {
        logger.warn("Issue creating URL: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Issue creating URL for redirection", request);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            NotFoundException ex, WebRequest request) {
        logger.warn("Short uri not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String message, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorDetails, status);
    }
}