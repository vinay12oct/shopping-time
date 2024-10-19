package com.shoppingtime.productservice.util;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${jwt.secret}") // Load the secret key from application properties or environment variables
	private String secretKey = "";

	// Get Secret Key for Signing/Validation
	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes); // Return secret key for signing
	}

	  // Extract username (email)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    
    // Extract roles from the JWT (roles stored as simple string array)
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles");
    }
    
    // Method to extract userId from the token
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.parseLong(claims.get("userId").toString()); // Extract userId from the claims
    }

	// Extract expiration date
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// General method to extract any claim from the token
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}

	// Check if the token has expired
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	 // Validate JWT token (this verifies the signature and checks if token is expired)
    public Boolean validateTokenSignature(String token) {
        try {
        	Jwts.parser()
        	.verifyWith(getKey())
        	.build()
        	.parseSignedClaims(token);// Will throw exception if signature is invalid
            return true;  // Check expiration
        } catch (Exception e) {
            return false;  // Invalid token
        }
    }

}
