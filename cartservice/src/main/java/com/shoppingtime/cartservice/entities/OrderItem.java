package com.shoppingtime.cartservice.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private Long productId;

    private int quantity;

    private BigDecimal price;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public OrderItem() {
		super();
		
	}


	public OrderItem(Long orderItemId, Long productId, int quantity, BigDecimal price, Order order) {
		super();
		this.orderItemId = orderItemId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.order = order;
	}

	@Override
	public String toString() {
		return "orderItemId=" + orderItemId + ", productId=" + productId + ", quantity=" + quantity + ", price=" + price + "";
	}

}


