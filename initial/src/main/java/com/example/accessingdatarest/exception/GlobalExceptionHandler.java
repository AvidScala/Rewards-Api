package com.example.accessingdatarest.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse ErrorResponse = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse ErrorResponse = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> duplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        ErrorResponse ErrorResponse = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorResponse ErrorResponse = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}