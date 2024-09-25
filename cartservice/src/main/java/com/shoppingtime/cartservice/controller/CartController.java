package com.shoppingtime.cartservice.controller;

import java.math.BigDecimal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingtime.cartservice.entities.Cart;
import com.shoppingtime.cartservice.service.CartService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(
        @RequestParam @NotNull(message = "User ID is required") Long userId, 
        @RequestParam @NotNull(message = "Product ID is required") Long productId, 
        @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity,
        @RequestParam @NotNull(message = "Price is required") BigDecimal price,
        @RequestParam @NotBlank(message = "Product name cannot be empty") String productName
    ) {
        Cart cart = cartService.addItemToCart(userId, productId, quantity, price, productName);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable @NotNull Long userId) {
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
    public ResponseEntity<Void> removeItemFromCart(
        @RequestParam @NotNull Long userId, 
        @RequestParam @NotNull Long productId
    ) {
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable @NotNull Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

