package com.shoppingtime.paymentservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentDTO {
    
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;
    
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
    
    @NotBlank(message = "Payment method cannot be empty")
    private String paymentMethod;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentDTO(@NotNull(message = "Order ID cannot be null") Long orderId,
			@DecimalMin(value = "0.01", message = "Amount must be greater than zero") @NotNull(message = "Amount cannot be null") BigDecimal amount,
			@NotBlank(message = "Payment method cannot be empty") String paymentMethod) {
		super();
		this.orderId = orderId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
	}

	public PaymentDTO() {
		super();
		
	}

	@Override
	public String toString() {
		return "orderId=" + orderId + ", amount=" + amount + ", paymentMethod=" + paymentMethod + "";
	}

    
}

