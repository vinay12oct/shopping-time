package com.shoppingtime.cartservice.dto;

import org.springframework.context.ApplicationEvent;

public class OrderCreatedEvent extends ApplicationEvent {

    private final Long orderId;
    private final String userEmail;

    public OrderCreatedEvent(Object source, Long orderId, String userEmail) {
        super(source);
        this.orderId = orderId;
        this.userEmail = userEmail;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
