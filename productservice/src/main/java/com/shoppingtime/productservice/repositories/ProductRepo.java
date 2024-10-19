package com.shoppingtime.productservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shoppingtime.productservice.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

	List<Product> findByCategory(String category);

	List<Product> findByBrand(String brand);

	Product findByName(String name);

	@Query("SELECT p FROM Product p WHERE " + "(p.name LIKE %:searchTerm% OR p.description LIKE %:searchTerm%) "
			+ "AND (:brand IS NULL OR p.brand = :brand) " + "AND (:category IS NULL OR p.category = :category)")
	List<Product> findBySearchAndFilter(@Param("searchTerm") String searchTerm, @Param("brand") String brand,
			@Param("category") String category);
}
