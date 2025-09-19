package com.escuelajavag4.inventory_service.exception;

public class ResourceHasDependenciesException extends RuntimeException {
    public ResourceHasDependenciesException(String message){
        super(message);
    }
}
