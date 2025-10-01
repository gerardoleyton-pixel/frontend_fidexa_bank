package com.example.exception.business;

/**
 * Se lanza cuando un recurso (usuario, cuenta, transacción) no existe en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
