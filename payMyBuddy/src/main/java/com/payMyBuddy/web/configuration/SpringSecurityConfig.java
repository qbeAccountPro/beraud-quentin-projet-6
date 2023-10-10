package com.paymybuddy.web.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers("/admin").hasRole("ADMIN")
        .requestMatchers("/user").hasRole("USER")
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login").defaultSuccessUrl("/AJOUTER REDIRECT URL PAGE de transfère", true).permitAll()
    /*
     * .and()
     * .oauth2Login()
     */;

    return http.build();
  }

  @Bean
  public UserDetailsManager users(DataSource dataSource) {
    UserDetails user = User.builder()
        .username("quentin")
        .password(passwordEncoder().encode("beraud"))
        .roles("USER")
        .build();

    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("beraud"))
        .roles("USER", "ADMIN")
        .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}