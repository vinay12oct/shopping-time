package com.ecommerce.userservice.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.ecommerce.userservice.dto.UserRegisteredEvent;

@Component
public class UserRegisteredEventPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publishUserRegisteredEvent(String userEmail) {
		UserRegisteredEvent registeredEvent = new UserRegisteredEvent(this, userEmail);
		applicationEventPublisher.publishEvent(registeredEvent);
	}

}
