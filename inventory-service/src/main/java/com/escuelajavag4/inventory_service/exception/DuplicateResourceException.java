package com.escuelajavag4.inventory_service.exception;

public class DuplicateResourceException extends  RuntimeException{
    public DuplicateResourceException(String message){
        super(message);
    }
}
