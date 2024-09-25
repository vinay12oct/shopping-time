package com.shoppingtime.cartservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingtime.cartservice.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	
	List<Order> findAllByUserId(Long userId);
}
