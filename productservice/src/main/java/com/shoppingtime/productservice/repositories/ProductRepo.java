package com.shoppingtime.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingtime.productservice.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
