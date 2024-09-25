package com.ecommerce.userservice.impl;

import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.repositories.UserRepo;
import com.ecommerce.userservice.security.UserPrincipal;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	
	// private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the repository
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " +username));
         return new UserPrincipal(user);
    }
}
