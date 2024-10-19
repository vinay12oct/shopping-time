package com.ecommerce.userservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.entities.Role;
import com.ecommerce.userservice.service.RoleService;
import com.ecommerce.userservice.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Autowired
	public RoleService roleService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	@Value("${jwt.secret}") // Load the secret key from application properties or environment variables
	private String secretKey = "";

	
	public String generateToken(String email,Long userId) {
		logger.info("user email ---------" + email);
		List<Role> roles = roleService.getRoleByEmail(email);
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles.stream().map(Role::getName).collect(Collectors.toList())); // Extract role names
		claims.put("userId", userId); // Add the userId to the token
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 100))
				.and()
				.signWith(getKey())
				.compact();

	}
	
	 // Get Secret Key for Signing/Validation
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); // Return secret key for signing
    }
	
	// Validate token
		public Boolean validateToken(String token, String email) {
			final String username = extractUsername(token);
			return (username.equals(email) && !isTokenExpired(token));
		}

		// Extract username from token
		public String extractUsername(String token) {
			return extractAllClaims(token).getSubject();
		}
		
		

		// Check if the token has expired
		private Boolean isTokenExpired(String token) {
			return extractExpiration(token).before(new Date());
		}

		// Extract expiration date
		public Date extractExpiration(String token) {
			return extractAllClaims(token).getExpiration();
		}

		

		private Claims extractAllClaims(String token) {
			return Jwts.parser()
					.verifyWith(getKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		}

}
