package com.ecommerce.userservice.dto;

import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent  extends ApplicationEvent {

    private final String userEmail;

	public UserRegisteredEvent(Object source,String userEmail) {
		super(source);
		
		this.userEmail = userEmail;
	}

    public String getUserEmail() {
        return userEmail;
    }

 

}
