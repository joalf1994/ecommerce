package com.escuelajavag4.catalogservice.exception;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String resource, Object identifier) {
        super("RESOURCE_NOT_FOUND", resource + " no encontrada: " + identifier);
    }
}
