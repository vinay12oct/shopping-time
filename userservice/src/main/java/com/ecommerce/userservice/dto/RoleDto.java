package com.ecommerce.userservice.dto;

public class RoleDto {

	private Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RoleDto(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public RoleDto() {
		
	}
	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + "";
	}
	
	
}
