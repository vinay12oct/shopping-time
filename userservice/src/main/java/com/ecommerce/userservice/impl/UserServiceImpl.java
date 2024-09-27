package com.ecommerce.userservice.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.dto.UserOrderDto;
import com.ecommerce.userservice.entities.Role;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exceptions.CartOperationException;
import com.ecommerce.userservice.exceptions.UserAlreadyExistsException;
import com.ecommerce.userservice.exceptions.UserNotFoundException;
import com.ecommerce.userservice.repositories.RoleRepo;
import com.ecommerce.userservice.repositories.UserRepo;
import com.ecommerce.userservice.service.UserService;
import com.shoppingtime.cartservice.dto.OrderDTO;
import com.shoppingtime.cartservice.entities.Cart;
import com.ecommerce.userservice.events.UserRegisteredEventPublisher;


@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
    private RestTemplate restTemplate;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserRegisteredEventPublisher userRegisteredEventPublisher;

	@Override
	public boolean registerUser(User user, List<String> roleNames) {

		logger.info("register method start ----------------------");

		// Check if the email already exists in the database
		if (userRepo.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException("Email is already taken.");
		}

		// Validate roleNames list
		if (roleNames == null || roleNames.isEmpty()) {
			throw new RuntimeException("At least one role must be provided.");
		}

		logger.info("email  : --------" + user.getEmail());
		logger.info("Size of roleNames: " + roleNames.size());
		roleNames.forEach(roleName -> logger.info("Processing role: [" + roleName + "]"));

		// Fetch or create Role objects based on the role names
		Set<Role> roles = roleNames.stream().map(roleName -> roleRepo.findByName(roleName).orElseGet(() -> {
			// If the role doesn't exist, create and save it
			Role newRole = new Role();
			newRole.setName(roleName.trim()); // Ensure no leading/trailing spaces
			logger.info("Creating new role: " + roleName);
			return roleRepo.save(newRole);
		})).collect(Collectors.toSet());

		// Log the roles
		roles.forEach(role -> logger.info("Role assigned: " + role.getName()));

		// Set roles to the user
		user.setRoles(roles);

		// Hash the user's password before saving to the database
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Save the user to the database
		userRepo.save(user);
		
		userRegisteredEventPublisher.publishUserRegisteredEvent(user.getEmail());

		return true; // Return true if registration is successful
	}

	@Override
	public User getUserById(Long id) {
		User user = userRepo.findById(id).get();
		return user;
	}

	@Override
	public String verify(User user) {
		// Validate input
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}

		try {
			// Attempt to authenticate the user
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

			// If authentication is successful, return a success message (you can replace
			// this with a JWT token)
			return jwtService.generateToken(user.getEmail());

		} catch (BadCredentialsException e) {
			// Handle bad credentials (incorrect username or password)
			return "Invalid email or password";

		} catch (AuthenticationException e) {
			// Handle other authentication exceptions
			return "Authentication failed: " + e.getMessage();
		}
	}

	@Override
	public UserDto getUserByEmail(String email) {

		 User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(
						"\"No user found with this email: \" + email"));
		 
		 UserDto userDto = new UserDto();
		 userDto.setEmail(user.getEmail());
		 userDto.setFullName(user.getFullName());
		 userDto.setPhoneNumber(user.getPhoneNumber());
		 userDto.setUserId(user.getId());
		 
	        // Map roles to a set of role names
	        Set<String> roleNames = user.getRoles().stream()
	            .map(Role::getName)
	            .collect(Collectors.toSet());
	        userDto.setRoles(roleNames);
		 
		return userDto;

	}
	
	@Override
	public Cart addToCart(String email, Long productId, int quantity, BigDecimal price, String productName) {
	    // Extract email from the token
	    User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(
						"\"No user found with this email: \" + email")); 
	   
	    // Get userId from user
	    Long userId = user.getId();

	    // Prepare the parameters for the API call
	    String cartServiceUrl = "http://CART-SERVICE/api/cart/add";
	    MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
	    params.add("userId", userId);
	    params.add("productId", productId);
	    params.add("quantity", quantity);
	    params.add("price", price);
	    params.add("productName", productName);

	    try {
	        // Call the Cart Service API to add the item to the cart
	        ResponseEntity<Cart> response = restTemplate.postForEntity(cartServiceUrl, params, Cart.class);
	        return response.getBody();
	    } catch (RestClientException e) {
	        // Log the error or throw a custom exception
	        throw new RuntimeException("Failed to add item to cart: " + e.getMessage());
	    }
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		 // Use Eureka to find the cart service by its service name
        String cartServiceUrl = "http://cart-service/api/cart/" + userId;

        try {
            // Make a GET request to the Cart service to get the cart by userId
            ResponseEntity<Cart> response = restTemplate.getForEntity(cartServiceUrl, Cart.class);
            return response.getBody();
        } catch (RestClientException e) {
            // Handle errors, such as if the cart service is not available or returns an error
            throw new CartOperationException("Failed to retrieve cart: " + e.getMessage());
        }
	}

	@Override
	public UserOrderDto createOrder(Long userId, String paymentType) {
	    // Define the URL of the API
	    String url = "http://localhost:8082/orders/create?userId=" + userId + "&paymentType=" + paymentType;

	    // Set up RestTemplate
	    RestTemplate restTemplate = new RestTemplate();

	    // Set headers (if needed)
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    // Build the request entity
	    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

	    try {
	        // Call the createOrder API
	        ResponseEntity<OrderDTO> response = restTemplate.postForEntity(url, requestEntity, OrderDTO.class);

	        // Return the response from the API
	        if (response.getStatusCode().is2xxSuccessful()) {
	            return convertOrderDTOToUserOrderDto(response.getBody());
	        } else {
	            // Handle failure case here (log, throw exception, etc.)
	            throw new RuntimeException("Failed to create order. Status code: " + response.getStatusCode());
	        }
	    } catch (Exception e) {
	        // Handle the exception (log, rethrow, etc.)
	        throw new RuntimeException("Error occurred while creating order: " + e.getMessage());
	    }
	}

	// Helper method to convert OrderDTO to UserOrderDto
	private UserOrderDto convertOrderDTOToUserOrderDto(OrderDTO orderDto) {
	    // Logic to convert OrderDTO to UserOrderDto based on your specific needs
	    UserOrderDto userOrderDto = new UserOrderDto();
	    // Set fields from orderDto to userOrderDto
	    userOrderDto.setOrderId(orderDto.getOrderId());
	    userOrderDto.setTransactionId(orderDto.getTransactionId());
	    // Add more fields as needed
	    return userOrderDto;
	}

	@Override
	public String getOrderByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
