package com.ecommerce.userservice.dto;

public class UserOrderDto {
	
	Long orderId;
	Long transactionId;
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
	public UserOrderDto(Long orderId, Long transactionId) {
		super();
		this.orderId = orderId;
		this.transactionId = transactionId;
	}
	public UserOrderDto() {
		super();
		
	}
	@Override
	public String toString() {
		return "orderId=" + orderId + ", transactionId=" + transactionId + "";
	}

}
