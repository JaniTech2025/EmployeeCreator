package com.example.employee.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {
    private HttpStatus status;

    public NotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    public NotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
