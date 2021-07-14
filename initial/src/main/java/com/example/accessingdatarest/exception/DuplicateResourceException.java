package com.example.accessingdatarest.exception;

import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Consumer;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String exception) {
        super(exception);
    }
}