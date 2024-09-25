package com.ecommerce.userservice.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	private String secretKey = "";

	public JwtService() {
		try {
			KeyGenerator instance = KeyGenerator.getInstance("HmacSHA256");
			SecretKey generateKey = instance.generateKey();
			secretKey = Base64.getEncoder().encodeToString(generateKey.getEncoded());
			logger.info(" secter key ---------" + secretKey);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
	}

	public String generateToken(String email) {
		logger.info("user email ---------" + email);
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10))
				.and()
				.signWith(getKey())
				.compact();

	}
	
	private SecretKey getKey() {
		byte[] decode = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(decode);
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
			return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
		}

}
