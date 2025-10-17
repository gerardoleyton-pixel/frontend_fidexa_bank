package com.example.exception.business;

/**
 * Excepción lanzada cuando no se encuentra una entidad solicitada.
 * Se utiliza para validar búsquedas por ID, correo, nombre de usuario, etc.
 */
public class ResourceNotFoundException extends RuntimeException {

      public ResourceNotFoundException(String entityName, Long id) {
        super(String.format("%s con ID %d no existe.", entityName, id));
    }


    public ResourceNotFoundException(String entityName, String fieldName, String fieldValue) {
        super(String.format("%s con %s '%s' no existe.", entityName, fieldName, fieldValue));
    }
}
