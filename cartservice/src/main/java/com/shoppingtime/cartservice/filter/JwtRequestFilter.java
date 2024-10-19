package com.shoppingtime.cartservice.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shoppingtime.cartservice.util.JwtUtil;

import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // Utility class for JWT parsing and verification

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String email = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Remove "Bearer " prefix
            try {
                if (jwtUtil.validateTokenSignature(jwt)) {
                    // Extract email (username) and roles from the token
                    email = jwtUtil.extractUsername(jwt);  // Subject from token
                    List<String> roles = jwtUtil.extractRoles(jwt);  // Extract roles from the token

                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                        // Convert roles (List<String>) to GrantedAuthority format for Spring Security
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                        // Create a UserDetails object manually with email and authorities
                        User userDetails = new User(email, "", authorities);

                        // Create an authentication object with the user's authorities
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set the authentication object in the SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (SignatureException e) {
                System.out.println("JWT signature validation failed");
            } catch (Exception e) {
                System.out.println("Error processing JWT token: " + e.getMessage());
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}
