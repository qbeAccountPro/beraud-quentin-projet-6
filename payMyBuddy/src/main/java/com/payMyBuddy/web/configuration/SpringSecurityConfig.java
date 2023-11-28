package com.paymybuddy.web.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Some javadoc :
 * 
 * This class represents the configuration of Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * Some javadoc :
   * 
   * This method represents the configuration of authentification fron a database.
   */
  @Autowired
  public void configAuthentification(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        .dataSource(dataSource)
        .usersByUsernameQuery("SELECT mail, password, 'true' FROM User WHERE mail=?")
        .authoritiesByUsernameQuery("SELECT mail, 'true' FROM User WHERE mail=?");
  }

  /**
   * Some javadoc :
   * 
   * This method represents the configuration of each authorized or not request on
   * the app.
   * Its detemine how and which file are authorized. The login page, the success
   * defauflt URL or many
   * other URL are dertimined here.
   * 
   * @param http represent the basic configuration.
   * 
   * @return the filter chain.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("transfer/**").permitAll();
      auth.requestMatchers("/css/**", "/js/**").permitAll();
      auth.anyRequest().authenticated();
    })
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll())

        .oauth2Login(oauth2Login -> oauth2Login
            .loginPage("/login")
            .defaultSuccessUrl("/transfer", true)
            .failureUrl("/login?error=true"))

        .rememberMe(rememberMe -> rememberMe
            .tokenRepository(persistentTokenRepository())
            .userDetailsService(userDetailsService))

        .formLogin(form -> form
            .loginPage("/login")
            .usernameParameter("email")
            .defaultSuccessUrl("/transfer", true)
            .permitAll())

        .httpBasic();

    return http.build();
  }

  /**
   * Some javadoc :
   * 
   * Service interface for encoding passwords. The preferred implementation is
   * BCryptPasswordEncoder
   * 
   * @return the encoding passwords.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Some javadoc :
   * 
   * Service to store persistent login tokens so that a user can log in
   * automatically next time.
   * 
   * @return the persistent login tokens for a user.
   */
  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }
}