package com.shoppingtime.cartservice.exception;

public class InvalidQuantityException extends RuntimeException {

	public InvalidQuantityException(String message) {
		super(message);
	}
}
