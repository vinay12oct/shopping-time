package com.ecommerce.userservice.controller;

import java.math.BigDecimal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.dto.UserRegistrationDto;
import com.ecommerce.userservice.entities.Role;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exceptions.CartOperationException;
import com.ecommerce.userservice.exceptions.InvalidTokenException;
import com.ecommerce.userservice.exceptions.UnauthorizedException;
import com.ecommerce.userservice.exceptions.UserAlreadyExistsException;
import com.ecommerce.userservice.impl.JwtService;
import com.ecommerce.userservice.service.UserService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private JwtService jwtService;
	
    @Autowired
    private UserService userService;
    
    
    @GetMapping("/user/get")
	public ResponseEntity<UserDto> getUserByEmail(@RequestHeader("Authorization") String token) {
		
		  if (token.startsWith("Bearer ")) {
	            token = token.substring(7);
	        }
		  String email = jwtService.extractUsername(token);
		
		  UserDto userDto = userService.getUserByEmail(email);
		
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

    @GetMapping("/public/welcome")
    public  String greet(){

        return  "Welcome to Shopping time !!!!!";
    }
    
    @GetMapping("/public/welcome/{id}")
    public  String getUserRole(@PathVariable Long id){
    	User user = userService.getUserById(id);
    	Set<Role> roles = user.getRoles();
        return user.toString() ;
    }
    
    
    @PostMapping("/public/login")
    public  String getLogin(@RequestBody User user){
    	
        return userService.verify(user) ;
    }

    @PostMapping("/public/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userDto) {
        try {
            // Basic validation
            if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
                return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
            }

            if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
                return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
            }


            User user = new User();
            user.setPassword(userDto.getPassword());
            user.setFullName(userDto.getFullName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setEmail(userDto.getEmail());
            // Register the user using the service
            userService.registerUser(user,userDto.getRoleNames());

            // If successful, return a 201 CREATED response
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);

        } catch (UserAlreadyExistsException e) {
            // If the user already exists, return a 409 CONFLICT response
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        } catch (Exception e) {
            // For any other exception, return a 500 INTERNAL SERVER ERROR response
            return new ResponseEntity<>("An error occurred while registering the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/user/welcome")
    public  String userGreet(){

        return  "Welcome !!!!!";
    }
    
   
   
}
