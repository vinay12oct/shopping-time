package com.ecommerce.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.dto.UserOrderDto;
import com.ecommerce.userservice.exceptions.InvalidTokenException;
import com.ecommerce.userservice.exceptions.UnauthorizedException;
import com.ecommerce.userservice.impl.JwtService;
import com.ecommerce.userservice.service.UserService;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class UserOrderController {
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    // Method renamed to be more descriptive
    @PostMapping("/user/order")
    public ResponseEntity<UserOrderDto> createOrder(
        @RequestHeader("Authorization") String token,
        @RequestParam @NotNull(message = "Payment type is required") String paymentType
    ) {
        // Validate token presence and format
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid token. Authorization header must contain a Bearer token.");
        }
        
        // Extract the token after "Bearer "
        token = token.substring(7);

        // Extract email from token using JWT service
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            throw new InvalidTokenException("Failed to extract email from token. Token might be invalid or expired.");
        }

        // Check if the email is valid
        if (email == null || email.isEmpty()) {
            throw new UnauthorizedException("Invalid token. Email not found in the token.");
        }

        // Fetch user details using the extracted email
        UserDto userDto = userService.getUserByEmail(email);
        Long userId = userDto.getUserId();

        // Call the service to create an order
        UserOrderDto userOrderDto = userService.createOrder(userId, paymentType);

        // Return the created order details
        return new ResponseEntity<>(userOrderDto, HttpStatus.CREATED);
    }
}
