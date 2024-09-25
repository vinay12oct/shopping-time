package com.shoppingtime.cartservice.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingtime.cartservice.entities.Cart;
import com.shoppingtime.cartservice.entities.CartItem;
import com.shoppingtime.cartservice.exception.CartNotFoundException;
import com.shoppingtime.cartservice.exception.InvalidQuantityException;
import com.shoppingtime.cartservice.exception.ProductNotFoundException;
import com.shoppingtime.cartservice.repositories.CartRepository;
import com.shoppingtime.cartservice.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart addItemToCart(Long userId, Long productId, int quantity, BigDecimal price, String productName) {
		// Retrieve the cart or create a new one if it doesn't exist
		Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
		cart.setUserId(userId);

		// Check if the product is already in the cart
		Optional<CartItem> existingItem = cart.getItems().stream().filter(item -> item.getProductId().equals(productId))
				.findFirst();

		// Update quantity if the product exists, otherwise add a new item
		if (existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		} else {
			CartItem newItem = new CartItem();
			newItem.setProductId(productId);
			newItem.setQuantity(quantity);
			newItem.setPrice(price);
			newItem.setProductName(productName);
			cart.getItems().add(newItem);
		}

		// Recalculate the total price of the cart
		cart.setTotalPrice(
				cart.getItems().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
						.reduce(BigDecimal.ZERO, BigDecimal::add));

		// Save and return the updated cart
		return cartRepository.save(cart);
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));
	}

	@Override
	public Cart addCartByUserId(Long userId) {
	    // Check if a cart already exists for the user
	    Optional<Cart> existingCart = cartRepository.findByUserId(userId);
	    
	    if (existingCart.isPresent()) {
	        throw new CartNotFoundException("Cart already exists for user with ID: " + userId);
	    }

	    // Create a new blank cart
	    Cart newCart = new Cart();
	    newCart.setUserId(userId);
	    newCart.setTotalPrice(BigDecimal.ZERO);  // Start with zero total price

	    // Save and return the newly created cart
	    return cartRepository.save(newCart);
	}


	@Override
	public Cart updateCartItem(Long userId, Long productId, int quantity) {
		// Retrieve the user's cart
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));

		// Find the product in the cart
		CartItem cartItem = cart.getItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst()
				.orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

		// Update the quantity, or throw an exception if the quantity is invalid
		if (quantity <= 0) {
			throw new InvalidQuantityException("Quantity must be greater than zero");
		}

		cartItem.setQuantity(quantity);

		// Recalculate the total price of the cart
		cart.setTotalPrice(
				cart.getItems().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
						.reduce(BigDecimal.ZERO, BigDecimal::add));

		// Save and return the updated cart
		return cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long userId, Long productId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));

		cart.getItems().removeIf(item -> item.getProductId().equals(productId));

		// Recalculate the total price
		cart.setTotalPrice(
				cart.getItems().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
						.reduce(BigDecimal.ZERO, BigDecimal::add));

		cartRepository.save(cart);
	}

	@Override
	public void clearCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));

		cart.getItems().clear();
		cart.setTotalPrice(BigDecimal.ZERO);
		cartRepository.save(cart);
	}

	
}
