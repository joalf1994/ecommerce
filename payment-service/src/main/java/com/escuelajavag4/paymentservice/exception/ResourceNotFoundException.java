package com.escuelajavag4.paymentservice.exception;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String resource, Object identifier) {
        super("RESOURCE_NOT_FOUND", resource + " no encontrada: " + identifier);
    }
}
