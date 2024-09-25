package com.shoppingtime.cartservice.service;

import java.util.List;

import com.shoppingtime.cartservice.dto.OrderDTO;
import com.shoppingtime.cartservice.entities.Order;

public interface OrderService {

    // Create a new order from the cart
	OrderDTO createOrder(Long userId,String paymentType);

    // Retrieve an order by ID
    OrderDTO getOrderById(Long orderId);

    // Retrieve all orders for a specific user
    List<OrderDTO> getOrdersByUserId(Long userId);

    // Update order status (e.g., from "Pending" to "Shipped")
    OrderDTO updateOrderStatus(Long orderId, String status);

    // Cancel an order
    void cancelOrder(Long orderId);

}

