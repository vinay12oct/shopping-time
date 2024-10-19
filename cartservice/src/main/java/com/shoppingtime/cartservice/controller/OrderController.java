package com.shoppingtime.cartservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

import com.shoppingtime.cartservice.dto.OrderDTO;
import com.shoppingtime.cartservice.exception.InvalidOrderException;
import com.shoppingtime.cartservice.service.OrderService;
import com.shoppingtime.cartservice.util.JwtUtil;

import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService,JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil     = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestHeader(value = "Authorization", required = true) String tokenHeader,
            @RequestParam(value = "paymentType", required = true) String paymentType) {

        try {
            // Check if the Authorization header contains the 'Bearer ' token
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or missing Authorization token");
            }

            // Extract the token by removing the 'Bearer ' prefix
            String token = tokenHeader.substring(7);

            // Extract the userId from the token using JwtService
            Long userId = jwtUtil.extractUserId(token);

            // Validate paymentType
            if (paymentType == null || paymentType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment type is required");
            }

            // Create the order and return the result
            
            try {
                OrderDTO orderDto = orderService.createOrder(userId, paymentType);
                if (orderDto == null) {
                    throw new InvalidOrderException("Order creation failed.");
                }
                return ResponseEntity.ok(orderDto);
            } catch (Exception e) {
            	logger.error("Error creating order", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
            }

        } catch (JwtException e) {
            // Handle JWT exceptions (invalid token, expired, etc.)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");

        } catch (InvalidOrderException e) {
            // Handle any order creation related exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create order: " + e.getMessage());

        } catch (Exception e) {
            // General error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}


