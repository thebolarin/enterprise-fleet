package com.enterprise.fleet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
//@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public CustomException(String message) {
        super(message);
        this.message = message; // Default body
        this.status = null; // Default status code
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.message = message; // Default body
        this.status = status; // Default status code
    }
}
