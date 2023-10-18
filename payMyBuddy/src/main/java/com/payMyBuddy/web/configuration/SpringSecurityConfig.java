package com.paymybuddy.web.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

  @Autowired
  private DataSource dataSource;

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        .dataSource(dataSource)
        .usersByUsernameQuery("SELECT firstName, password, 'true' FROM User WHERE firstName=?")
        .authoritiesByUsernameQuery("SELECT firstName, 'ROLE_USER' FROM User WHERE firstName=?");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> {
      auth.anyRequest().authenticated();
    }).logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll()
    .and().oauth2Login().loginPage("/login").defaultSuccessUrl("/home").failureUrl("/login?error=true")
    .and().formLogin(form -> form.loginPage("/login").permitAll());

    return http.build();
  }

  /*
   * @Bean
   * public UserDetailsManager users(DataSource dataSource) {
   * UserDetails user = User.builder()
   * .username("quentin")
   * .password("$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS")
   * .roles("USER")
   * .build();
   * 
   * UserDetails admin = User.builder()
   * .username("admin")
   * .password(passwordEncoder().encode("admin"))
   * .roles("USER", "ADMIN")
   * .build();
   * 
   * return new InMemoryUserDetailsManager(user, admin);
   * }
   */

  /*
   * @Bean
   * public PasswordEncoder passwordEncoder() {
   * return new BCryptPasswordEncoder();
   * }
   */
}