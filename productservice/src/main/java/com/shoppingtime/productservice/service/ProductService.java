package com.shoppingtime.productservice.service;

import java.util.List;

import com.shoppingtime.productservice.entities.Product;

public interface ProductService {

    
    boolean addNewProduct(Product product);

   
    Product getProductById(Long id);

    
    List<Product> getAllProducts();

   
    void deleteProduct(Long id);

    
    Product updateProduct(Product product, Long id);
}
