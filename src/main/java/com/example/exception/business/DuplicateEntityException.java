package com.example.exception.business;

/**
 * Se lanza cuando se intenta crear un recurso que ya existe (por ejemplo, email o número de cuenta duplicado).
 */
public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
