package com.ecommerce.userservice.dto;

import java.util.Set;

public class UserDto {
    private Long userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Set<String> roles;  // Store only role names or IDs
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public UserDto(Long userId, String email, String fullName, String phoneNumber, Set<String> roles) {
		super();
		this.userId = userId;
		this.email = email;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
	}
	public UserDto() {
		
	}
	@Override
	public String toString() {
		return "userId=" + userId + ", email=" + email + ", fullName=" + fullName + ", phoneNumber="
				+ phoneNumber + ", roles=" + roles + "";
	}

    
}
