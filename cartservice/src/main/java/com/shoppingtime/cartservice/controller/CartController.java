package com.shoppingtime.cartservice.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingtime.cartservice.entities.Cart;
import com.shoppingtime.cartservice.service.CartService;
import com.shoppingtime.cartservice.util.JwtUtil;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/addItems")
    public ResponseEntity<Cart> addItemToCart(
        @RequestHeader("Authorization") String tokenHeader, // Accept the token from the header
        @RequestParam @NotNull(message = "Product ID is required") Long productId, 
        @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity,
        @RequestParam @NotNull(message = "Price is required") BigDecimal price,
        @RequestParam @NotBlank(message = "Product name cannot be empty") String productName
    ) {
        // Extract the token by removing the 'Bearer ' prefix
        String token = tokenHeader.substring(7);
        
        // Extract the userId from the token using JwtService
        Long userId = jwtUtil.extractUserId(token);
        
        // Log userId for verification (optional)
        logger.info("Extracted User ID: " + userId);

        // Pass the userId to the service layer along with other parameters
        Cart cart = cartService.addItemToCart(userId, productId, quantity, price, productName);
        
        return ResponseEntity.ok(cart);
    }


    @GetMapping("/getCart")
    public ResponseEntity<Cart> getCart( @RequestHeader("Authorization") String tokenHeader) {
    	
    	 // Extract the token by removing the 'Bearer ' prefix
        String token = tokenHeader.substring(7);
        
        // Extract the userId from the token using JwtService
        Long userId = jwtUtil.extractUserId(token);
        
        // Log userId for verification (optional)
        logger.info("Extracted User ID: " + userId);
    	
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/{userId}")
    public ResponseEntity<Cart> addNewCart(@PathVariable @NotNull Long userId) {
        Cart cart = cartService.addCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCartItem(
        @RequestParam @NotNull Long userId, 
        @RequestParam @NotNull Long productId, 
        @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity
    ) {
        Cart cart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItemFromCart(@RequestHeader("Authorization") String tokenHeader,
      
        @RequestParam @NotNull Long productId
    ) {
    	 // Extract the token by removing the 'Bearer ' prefix
        String token = tokenHeader.substring(7);
        
        // Extract the userId from the token using JwtService
        Long userId = jwtUtil.extractUserId(token);
        
        // Log userId for verification (optional)
        logger.info("Extracted User ID: " + userId);
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable @NotNull Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

