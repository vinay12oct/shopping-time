package com.shoppingtime.productservice.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingtime.productservice.entities.Product;
import com.shoppingtime.productservice.exceptions.ProductNotFoundException;
import com.shoppingtime.productservice.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    
    @GetMapping("/public/welcome")
    public  String greet(){

        return  "Welcome to Shopping time we are in product service !!!!!";
    }
    
    @GetMapping("/user/welcome")
    public  String userGreet(){

        return  "Welcome to Product Service !!!!!";
    }
    

    // Create a new product

    @PostMapping("/product/add")
    public ResponseEntity<String> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("category") String category,
            @RequestParam("brand") String brand,
            @RequestParam("isAvailable") boolean isAvailable,
            @RequestParam("addedDate") LocalDateTime addedDate,
            @RequestParam("image") MultipartFile image) {

        try {
            // Convert image to base64 or handle image storage as needed
            byte[] imageData = image.getBytes();
            String imageType = image.getContentType();
            String imageName = image.getOriginalFilename();

            // Create Product object
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setStock(stock);
            product.setCategory(category);
            product.setBrand(brand);
            product.setAvailable(isAvailable);
            product.setAddedDate(addedDate);
            product.setImageData(imageData);
            product.setImageType(imageType);
            product.setImageName(imageName);

            // Call service to save product
            boolean isAdded = productService.addNewProduct(product);
            if (isAdded) {
                return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to add product", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing image upload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get product by ID
    @GetMapping("/user/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // Get all products
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    // search And filter
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category) {
        return productService.searchProducts(searchTerm, brand, category);
    }

    // Update product by ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long id) {
        Product updatedProduct = productService.updateProduct(product, id);
        if (updatedProduct != null) {
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
    }

    // Delete product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}
