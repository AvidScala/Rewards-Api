package com.example.accessingdatarest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class MethodArgumentNotValidException extends RuntimeException
{
    public MethodArgumentNotValidException(String exception) {
        super(exception);
    }
}