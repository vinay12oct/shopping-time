package com.ecommerce.userservice.exceptions;

public class UnauthorizedException extends RuntimeException {
	
    public UnauthorizedException(String message) {
        super(message);
    }
}
