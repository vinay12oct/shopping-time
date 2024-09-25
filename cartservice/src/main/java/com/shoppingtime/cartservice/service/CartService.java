package com.shoppingtime.cartservice.service;

import java.math.BigDecimal;

import com.shoppingtime.cartservice.entities.Cart;

public interface CartService {

    Cart addItemToCart(Long userId, Long productId, int quantity, BigDecimal price, String productName);
    
  //  Cart updateCartAddNewItemToCart(Long userId, Long productId, int quantity, BigDecimal price, String productName);
    
    Cart getCartByUserId(Long userId);
    
    Cart addCartByUserId(Long userId);
    
    Cart updateCartItem(Long userId, Long productId, int quantity);

    void removeItemFromCart(Long userId, Long productId);

    void clearCart(Long userId);
    
    
}
