package com.example.accessingdatarest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class Exception extends RuntimeException
{
    public Exception(String exception) {
        super(exception);
    }
}