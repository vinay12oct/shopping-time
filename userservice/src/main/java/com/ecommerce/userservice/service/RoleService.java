package com.ecommerce.userservice.service;

import java.util.List;

import com.ecommerce.userservice.entities.Role;

public interface RoleService {
	
	List<Role> getRoleByEmail(String email);

}
