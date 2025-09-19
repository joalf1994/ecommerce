package com.escuelajavag4.order_service.exception;

public class StockInsufficientException extends RuntimeException{
    public StockInsufficientException(String message) {
        super(message);
    }
}
