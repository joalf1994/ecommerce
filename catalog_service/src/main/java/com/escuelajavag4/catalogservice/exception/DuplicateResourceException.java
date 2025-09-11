package com.escuelajavag4.catalogservice.exception;

public class DuplicateResourceException extends AppException{
    public DuplicateResourceException(String resource, String field, Object value) {
        super("DUPLICATE_RESOURCE", resource + " ya existe con " + field + ": " + value);
    }
}
