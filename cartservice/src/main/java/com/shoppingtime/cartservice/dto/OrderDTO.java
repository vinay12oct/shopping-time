package com.shoppingtime.cartservice.dto;

import com.shoppingtime.cartservice.entities.Order;

public class OrderDTO {

	private Long userId;
	private Long orderId;
	public OrderDTO(Long userId, Long orderId, String paymentType, Long transactionId) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.paymentType = paymentType;
		this.transactionId = transactionId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	private String paymentType;
	private Long transactionId;

	// Convert from entity to DTO
	public static OrderDTO from(Order order) {
		OrderDTO dto = new OrderDTO();
		dto.setUserId(order.getUserId());
		dto.setOrderId(order.getOrderId());
		dto.setTransactionId(order.getTransactionId());
		dto.setPaymentType(order.getPaymentType());
		return dto;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	
	public OrderDTO() {
		super();

	}

}
