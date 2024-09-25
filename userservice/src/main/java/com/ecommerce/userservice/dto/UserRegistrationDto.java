package com.ecommerce.userservice.dto;

import java.util.List;


public class UserRegistrationDto {

    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<String> roleNames;
    
    
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}
	public UserRegistrationDto(String password, String fullName, String phoneNumber, String email,
			List<String> roleNames) {
		super();
		this.password = password;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.roleNames = roleNames;
	}
	public UserRegistrationDto() {
		super();
		
	}
	@Override
	public String toString() {
		return "password=" + password + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", roleNames=" + roleNames + "";
	}



}
