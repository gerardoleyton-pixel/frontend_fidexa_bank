package com.example.exception.business;

/**
 * Excepción lanzada cuando se intenta crear una entidad duplicada.
 * Se utiliza para validar campos únicos como correo electrónico o número de cuenta.
 */
public class DuplicateEntityException extends RuntimeException {


    public DuplicateEntityException(String entityName, String fieldName, String fieldValue) {
        super(String.format("%s con %s '%s' ya existe.", entityName, fieldName, fieldValue));
    }
}
