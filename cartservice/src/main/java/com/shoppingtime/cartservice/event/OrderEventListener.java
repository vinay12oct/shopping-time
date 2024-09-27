package com.shoppingtime.cartservice.event;



import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.shoppingtime.cartservice.dto.OrderCreatedEvent;


@Component
public class OrderEventListener {

    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        // Handle the event, e.g., send an email
        System.out.println("Received order created event - Order ID: " + event.getOrderId() + ", Email: " + event.getUserEmail());
        sendEmailNotification(event.getUserEmail(), event.getOrderId());
    }

    private void sendEmailNotification(String email, Long orderId) {
        // Simulate sending an email (replace with actual email sending logic)
        System.out.println("Sending email to " + email + " for order " + orderId);
    }
}

