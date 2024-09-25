package com.ecommerce.userservice.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.exceptions.CartOperationException;
import com.ecommerce.userservice.exceptions.InvalidTokenException;
import com.ecommerce.userservice.exceptions.UnauthorizedException;
import com.ecommerce.userservice.impl.JwtService;
import com.ecommerce.userservice.service.UserService;
import com.shoppingtime.cartservice.entities.Cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class UserCartController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@GetMapping("/user/cart")
	public ResponseEntity<Cart> getCartByUser(@RequestHeader("Authorization") String token) {

		// Validate the token presence and format
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

		UserDto userDto = userService.getUserByEmail(email);
		Long userId = userDto.getUserId();
		Cart cartByUserId = userService.getCartByUserId(userId);

		return new ResponseEntity<>(cartByUserId, HttpStatus.FOUND);

	}
	
	 @PostMapping("/user/cart/add")
	    public ResponseEntity<String> addProductInCart(
	        @RequestHeader("Authorization") String token,
	        @RequestParam @NotNull(message = "Product ID is required") Long productId,
	        @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity,
	        @RequestParam @NotNull(message = "Price is required") BigDecimal price,
	        @RequestParam @NotBlank(message = "Product name cannot be empty") String productName) {

	        // Validate the token presence and format
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

	        // Add product to cart using the extracted email
	       
	        try {
	           userService.addToCart(email, productId, quantity, price, productName);
	        } catch (Exception e) {
	            throw new CartOperationException("Failed to add product to cart: " + e.getMessage());
	        }

	        // Return successful response
	        return new ResponseEntity<>("Product added to cart successfully.", HttpStatus.CREATED);
	    }
	    

}
