package com.ecommerce.userservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.entities.Role;
import com.ecommerce.userservice.repositories.RoleRepo;
import com.ecommerce.userservice.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public List<Role> getRoleByEmail(String email) {
     
		List<Role> roleByEmail = roleRepo.findRolesByEmail(email);

		return roleByEmail;
	}

}
