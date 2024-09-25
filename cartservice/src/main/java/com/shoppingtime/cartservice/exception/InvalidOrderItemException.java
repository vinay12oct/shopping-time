package com.shoppingtime.cartservice.exception;

public class InvalidOrderItemException extends RuntimeException {

    public InvalidOrderItemException(String message) {
        super(message);
    }

    public InvalidOrderItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
