package com.escuelajavag4.inventory_service.exception;

public class ResourceNotFoundException extends AppException{
    public ResourceNotFoundException(String resource, Object identifier) {
        super("RESOURCE_NOT_FOUND", resource + " no encontrada: " + identifier);
    }
}
