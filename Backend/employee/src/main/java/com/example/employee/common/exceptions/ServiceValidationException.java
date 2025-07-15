package com.example.employee.common.exceptions;

public class ServiceValidationException extends RuntimeException {

    private ValidationErrors errors;

    public ServiceValidationException(ValidationErrors errors) {
        super();
        this.errors = errors;
    }

    public ValidationErrors getErrors() {
        return this.errors;
    }
}
