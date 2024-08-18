package com.mizitoh.webflux.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
public class ValidationError extends StandardError {

    private final List<FieldError> errors = new ArrayList<>();
    ValidationError(LocalDateTime timeStamp, String path, Integer status, String error, String message) {
        super(timeStamp, path, status, error, message);
    }

    public void addError(String fieldName, String message) {
        this.errors.add(new FieldError(fieldName, message));
    }
    @Getter
    @AllArgsConstructor
    private static final class FieldError {
        private String fieldName;
        private String message;
    }
}
