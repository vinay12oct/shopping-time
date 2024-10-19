package com.ecommerce.userservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.userservice.entities.Role;

public interface RoleRepo extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
    
    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.email = :email")
    List<Role> findRolesByEmail(@Param("email") String email);

}
