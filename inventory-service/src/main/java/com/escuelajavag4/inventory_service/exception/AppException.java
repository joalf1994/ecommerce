package com.escuelajavag4.inventory_service.exception;

public abstract class AppException extends RuntimeException{
    private final String code;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
