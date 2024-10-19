package com.shoppingtime.productservice.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shoppingtime.productservice.filter.JwtRequestFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtRequestFilter jwtFilter;


    // Security filter chain to define access rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf(csrf -> csrf.disable())  // Updated CSRF disabling method
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()  // Allow public access without authentication
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")  // Only accessible by ADMIN role
                        .requestMatchers("/api/products/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()  // All other endpoints require authentication
                )
                .httpBasic(withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);   // Use HTTP Basic Authentication for Postman

        return http.build();
    }
    
    
}
