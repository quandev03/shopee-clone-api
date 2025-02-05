package com.example.banhangapi.helper.handleException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateProductNameException extends RuntimeException {
    public DuplicateProductNameException(String message) {
        super(message);
    }
}
