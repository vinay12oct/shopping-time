package com.shoppingtime.cartservice.dto;

import java.math.BigDecimal;

public class DtoForNewOrder {

	private Long orderId;
	private Long userId;
	private BigDecimal totalPrice;
	private String paymentType;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public DtoForNewOrder(Long orderId, Long userId, BigDecimal totalPrice, String paymentType) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.paymentType = paymentType;
	}
	public DtoForNewOrder() {
		
	}
	@Override
	public String toString() {
		return "orderId=" + orderId + ", userId=" + userId + ", totalPrice=" + totalPrice
				+ ", paymentType=" + paymentType + "";
	}
	
	

}
