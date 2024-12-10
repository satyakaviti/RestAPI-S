package com.payments.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends CustomException {
        public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
            super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue));
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class CustomerAlreadyExistsException extends CustomException {
        public CustomerAlreadyExistsException(String message) {
            super(message);
        }
    }
}
