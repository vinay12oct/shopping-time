package com.shoppingtime.productservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingtime.productservice.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
	
	List<Product> findByCategory(String category);


}
