package com.ecommerce.userservice.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ecommerce.userservice.dto.UserRegisteredEvent;
import com.ecommerce.userservice.impl.NotificationService;

@Component
public class UserRegisteredEventListener {

    @Autowired
    private NotificationService notificationService; // Inject the NotificationService

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent registeredEvent) {
        // Handle the event and send an email
        System.out.println("User Registered Successfully with Email: " + registeredEvent.getUserEmail());

        // Call NotificationService to send an email
        notificationService.sendEmail(registeredEvent.getUserEmail());
    }
}
