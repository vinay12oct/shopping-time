package com.shoppingtime.cartservice.dto;

import java.math.BigDecimal;

public class Dto {
	
	private BigDecimal totalPrice;
	private String paymentType;
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

}
