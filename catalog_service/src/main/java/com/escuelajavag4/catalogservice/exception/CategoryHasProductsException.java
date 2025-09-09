package com.escuelajavag4.catalogservice.exception;

public class CategoryHasProductsException extends RuntimeException{
    public CategoryHasProductsException(String message) {
        super(message);
    }

    public CategoryHasProductsException(String message, Throwable cause) {
        super(message, cause);
    }
}
