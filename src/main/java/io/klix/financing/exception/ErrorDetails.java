package io.klix.financing.exception;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
    private Map<String, String> fieldErrors;  // New field to store validation errors

    public ErrorDetails(Date timestamp, String message, String details, Map<String, String> fieldErrors) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.fieldErrors = fieldErrors;
    }

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}