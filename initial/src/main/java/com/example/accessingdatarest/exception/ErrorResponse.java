package com.example.accessingdatarest.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@Getter
public class ErrorResponse {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}