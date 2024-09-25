package com.shoppingtime.cartservice.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private BigDecimal totalPrice;

    private String status; // e.g., "Pending", "Shipped", "Delivered"

    private String paymentType; // e.g., "Credit Card", "PayPal", "Cash on Delivery"

    private boolean paymentStatus; // true if payment is successful, false if pending or failed

    private Long transactionId; // Transaction ID for successful online payments

    private LocalDateTime orderDate; // Timestamp when the order is placed

    public Order() {
        
    }

    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public Order(Long OrderId, Long userId, List<OrderItem> items, BigDecimal totalPrice, String status, 
                 String paymentType, boolean paymentStatus, Long transactionId, LocalDateTime orderDate) {
        this.orderId = OrderId;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.orderDate = orderDate;
    }

	@Override
	public String toString() {
		return "OrderId=" + orderId + ", userId=" + userId + ", items=" + items + ", totalPrice=" + totalPrice
				+ ", status=" + status + ", paymentType=" + paymentType + ", paymentStatus=" + paymentStatus
				+ ", transactionId=" + transactionId + ", orderDate=" + orderDate + "";
	}

   
	
}

