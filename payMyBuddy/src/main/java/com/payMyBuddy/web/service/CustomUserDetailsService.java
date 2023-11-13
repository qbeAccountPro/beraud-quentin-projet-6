package com.paymybuddy.web.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.web.repository.UserRepository;

// Your service should implement UserDetailsService
@Service
public class CustomUserDetailsService implements UserDetailsService {

  // Inject your UserRepository or DataSource to fetch user details
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Fetch user details from your data source based on email
    com.paymybuddy.web.model.User user = userRepository.findByMail(email);

    if (user == null) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }

    // Create a UserDetails object using the fetched user details
    return User.withUsername(user.getMail())
        .password(user.getPassword()) // Use your password field
        .roles("USER") // Set user roles if needed
        .build();
  }
}