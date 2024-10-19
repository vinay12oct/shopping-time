package com.shoppingtime.productservice.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingtime.productservice.entities.Product;
import com.shoppingtime.productservice.exceptions.ProductNotFoundException;
import com.shoppingtime.productservice.exceptions.ProductServiceException;
import com.shoppingtime.productservice.repositories.ProductRepo;
import com.shoppingtime.productservice.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public boolean addNewProduct(Product product) {
        try {
            productRepo.save(product);
            return true;
        } catch (Exception e) {
            throw new ProductServiceException("Failed to add new product", e);
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepo.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Failed to retrieve all products", e);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            if (productRepo.existsById(id)) {
                productRepo.deleteById(id);
            } else {
                throw new ProductNotFoundException("Product with id " + id + " does not exist");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to delete product with id " + id, e);
        }
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        try {
            Optional<Product> existingProduct = productRepo.findById(id);
            if (existingProduct.isPresent()) {
                Product updatedProduct = existingProduct.get();
                updatedProduct.setName(product.getName());
                updatedProduct.setPrice(product.getPrice());
                updatedProduct.setDescription(product.getDescription());
                updatedProduct.setStock(product.getStock());
                // Update other fields as necessary
                return productRepo.save(updatedProduct);
            } else {
                throw new ProductNotFoundException("Product with id " + id + " not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to update product with id " + id, e);
        }
    }

	@Override
	public List<Product> searchProducts(String searchTerm, String brand, String category) {
		
		return productRepo.findBySearchAndFilter(searchTerm, brand, category);
	}
}

