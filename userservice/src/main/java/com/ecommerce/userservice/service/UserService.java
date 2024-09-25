package com.ecommerce.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.dto.UserOrderDto;
import com.ecommerce.userservice.entities.User;
import com.shoppingtime.cartservice.entities.Cart;

public interface UserService {

    boolean registerUser(User user, List<String> roleNames);
    
    User getUserById(Long id);

	String verify(User user);
	
	UserDto getUserByEmail(String email);
	
    Cart addToCart(String email,Long productId, int quantity, BigDecimal price, String productName);

    Cart getCartByUserId(Long userId);
    
    UserOrderDto createOrder(Long userId,String paymentType);
    
    String getOrderByOrderId(Long orderId);
}
