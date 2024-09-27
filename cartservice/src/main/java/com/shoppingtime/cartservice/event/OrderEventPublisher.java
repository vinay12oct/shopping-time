package com.shoppingtime.cartservice.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.shoppingtime.cartservice.dto.OrderCreatedEvent;


@Component
public class OrderEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishOrderCreatedEvent(Long orderId, String userEmail) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(this, orderId, userEmail);
        applicationEventPublisher.publishEvent(orderCreatedEvent);
    }
}



