package com.shoppingtime.paymentservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {
	
    @Id
    private Long transactionId;
    
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Payment(Long transactionId, Long orderId, BigDecimal amount, String paymentMethod, String status) {
		super();
		this.transactionId = transactionId;
		this.orderId = orderId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.status = status;
	}
	public Payment() {
		super();
		
	}
	@Override
	public String toString() {
		return "transactionId=" + transactionId + ", orderId=" + orderId + ", amount=" + amount + ", paymentMethod=" + paymentMethod
				+ ", status=" + status + "";
	}

   
}
