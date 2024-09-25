package com.shoppingtime.cartservice.dto;

import java.math.BigDecimal;

import com.shoppingtime.cartservice.entities.OrderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemDTO {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be at least 0")
    private BigDecimal price;

    // Constructors
    public OrderItemDTO() {
    }

    public OrderItemDTO(Long productId, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // Static method to convert from OrderItem entity to OrderItemDTO
    public static OrderItemDTO from(OrderItem orderItem) {
        return new OrderItemDTO(
            orderItem.getProductId(),  // Assuming OrderItem has a Product reference
            orderItem.getQuantity(),
            orderItem.getPrice()
        );
    }
}

