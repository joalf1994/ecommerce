package com.escuelajavag4.order_service.exception;

public class OrderItemNotFoundException extends RuntimeException{
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
