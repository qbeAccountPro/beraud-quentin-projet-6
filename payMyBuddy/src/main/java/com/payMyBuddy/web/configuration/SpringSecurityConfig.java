package com.paymybuddy.web.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Autowired
  private DataSource dataSource;

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        .dataSource(dataSource)
        .usersByUsernameQuery("SELECT firstName, password, 'true' FROM User WHERE firstName=?")
        .authoritiesByUsernameQuery("SELECT firstName, 'true' FROM User WHERE firstName=?");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("/transfer/addConnection").permitAll().anyRequest().authenticated();
    }).logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll()
        .and().oauth2Login().loginPage("/login").defaultSuccessUrl("/transfer", true).failureUrl("/login?error=true")
        .and().formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/transfer", true).permitAll()).csrf()
        .disable().cors(); // TODO voir avec réda le csrf().disable().cors() qui empeche l'erreure 403 forbiden impossible sans comment faire ? méthode en attendant.

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}