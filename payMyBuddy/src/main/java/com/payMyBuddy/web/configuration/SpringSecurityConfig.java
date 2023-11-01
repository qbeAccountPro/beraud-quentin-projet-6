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
        .usersByUsernameQuery("SELECT mail, password, 'true' FROM User WHERE mail=?")
        .authoritiesByUsernameQuery("SELECT mail, 'true' FROM User WHERE mail=?");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("transfer/**").permitAll();
      auth.requestMatchers("/css/**", "/js/**").permitAll();
      auth.anyRequest().authenticated();

    }).logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .permitAll()
        .and()
        .oauth2Login()
        .loginPage("/login")
        .defaultSuccessUrl("/transfer", true)
        .failureUrl("/login?error=true")
        .and()
        .httpBasic()
        .and()
        .formLogin(form -> form.loginPage("/login")
            .usernameParameter("email")
            .defaultSuccessUrl("/transfer", true)
            .permitAll());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}