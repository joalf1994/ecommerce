package com.escuelajavag4.inventory_service.exception;

public class StockInsufficientException extends  RuntimeException{
    public StockInsufficientException(String message) {
        super(message);
    }
}
