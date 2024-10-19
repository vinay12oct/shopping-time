package com.shoppingtime.cartservice.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.shoppingtime.cartservice.dto.OrderDTO;
import com.shoppingtime.cartservice.entities.Cart;
import com.shoppingtime.cartservice.entities.Order;
import com.shoppingtime.cartservice.entities.OrderItem;
import com.shoppingtime.cartservice.event.OrderEventPublisher;
import com.shoppingtime.cartservice.exception.InvalidOrderException;
import com.shoppingtime.cartservice.exception.OrderNotFoundException;
import com.shoppingtime.cartservice.exception.PaymentFailedException;
import com.shoppingtime.cartservice.repositories.OrderRepository;
import com.shoppingtime.cartservice.service.CartService;
import com.shoppingtime.cartservice.service.OrderService;
import com.shoppingtime.paymentservice.dto.PaymentDTO;
import com.shoppingtime.paymentservice.entity.Payment;



@Service
public class OrderServiceImpl implements OrderService {
	
	private final OrderEventPublisher orderEventPublisher;
	private final OrderRepository orderRepository;
	private final CartService cartService;

	public OrderServiceImpl(OrderRepository orderRepository, CartService cartService,OrderEventPublisher orderEventPublisher) {
		this.orderRepository = orderRepository;
		this.cartService = cartService;
		this.orderEventPublisher = orderEventPublisher;
	}

	@Transactional
	@Override
	public OrderDTO createOrder(Long userId, String paymentType) {
	    // Logic to convert cart items into an order and save to the database
	    Cart cart = cartService.getCartByUserId(userId);

	    if (cart == null || cart.getItems().isEmpty()) {
	        throw new InvalidOrderException("Cart is empty");
	    }

	    // Create Order object
	    Order order = new Order();
	    order.setUserId(userId);

	    // Map Cart items to Order items
	    List<OrderItem> orderItems = cart.getItems().stream()
	        .map(cartItem -> {
	            // Create a new OrderItem object
	            OrderItem orderItem = new OrderItem();

	            // Set the product ID, quantity, and price from cartItem
	            orderItem.setProductId(cartItem.getProductId());
	            orderItem.setQuantity(cartItem.getQuantity());
	            orderItem.setPrice(cartItem.getPrice());

	            // Set the order for each OrderItem
	            orderItem.setOrder(order); // Ensures the bidirectional relationship is set

	            return orderItem; // Return the created OrderItem
	        })
	        .collect(Collectors.toList());

	    // Set the order's items
	    order.setItems(orderItems);

	    order.setTotalPrice(cart.getTotalPrice());
	    order.setStatus("Pending");
	    order.setOrderDate(LocalDateTime.now());

	    // Save the order first to generate the orderId
	    Order savedOrder = orderRepository.save(order);

	    // Check if the payment type is "ONLINE"
	    if (paymentType.matches("ONLINE")) {
	        // Create PaymentDTO object
	        PaymentDTO paymentDTO = new PaymentDTO();
	        paymentDTO.setOrderId(order.getOrderId());
	        paymentDTO.setAmount(order.getTotalPrice());
	        paymentDTO.setPaymentMethod("ONLINE");

	        // Prepare the request
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<PaymentDTO> requestEntity = new HttpEntity<>(paymentDTO, headers);

	        try {
	            // Call the payment service using POST method
	            ResponseEntity<Payment> response = restTemplate.postForEntity(
	                "http://localhost:8083/api/payments", // Payment service URL
	                requestEntity,
	                Payment.class // Expecting Payment object as response
	            );

	            // Check the response and update the order accordingly
	            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	                Payment payment = response.getBody();
	                if (payment.getStatus().equalsIgnoreCase("COMPLETED")) { // Assuming Payment has this method
	                    savedOrder.setPaymentStatus(true);
	                    savedOrder.setTransactionId(payment.getTransactionId());
	                    savedOrder.setStatus("COMPLETED");
	                    savedOrder.setPaymentType("ONLINE");
	                } else {
	                    savedOrder.setPaymentStatus(false);
	                    throw new PaymentFailedException("Payment failed, transaction ID: " + payment.getTransactionId());
	                }
	            } else {
	                throw new PaymentFailedException("Payment service returned an error: " + response.getStatusCode());
	            }
	        } catch (Exception e) {
	            throw new PaymentFailedException("Payment service error: " + e.getMessage());
	        }
	    } else if (paymentType.equalsIgnoreCase("CASH ON DELIVERY")) {
	        // Handling for Cash on Delivery (COD)
	        savedOrder.setPaymentStatus(false);  // No payment done upfront
	        savedOrder.setStatus("PENDING DELIVERY"); // COD orders are pending until delivered
	        savedOrder.setPaymentType("CASH ON DELIVERY");
	    }

	    // Save the order with the final status
	    orderRepository.save(savedOrder);

	    // Clear the cart after the order is created
	    cartService.clearCart(userId);

	    // Convert the saved order to OrderDTO
	    OrderDTO dto = OrderDTO.from(savedOrder);

	    return dto;
	}

	@Override
	public OrderDTO getOrderById(Long orderId) {
		Optional<Order> order = orderRepository.findById(orderId);
		return order.map(OrderDTO::from).orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}

	@Override
	public List<OrderDTO> getOrdersByUserId(Long userId) {
		List<Order> orders = orderRepository.findAllByUserId(userId);
		return orders.stream().map(OrderDTO::from).toList();
	}

	@Override
	public OrderDTO updateOrderStatus(Long orderId, String status) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));
		order.setStatus(status);
		return OrderDTO.from(orderRepository.save(order));
	}

	@Override
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found"));
		order.setStatus("Cancelled");
		orderRepository.save(order);
	}
	
	
}
