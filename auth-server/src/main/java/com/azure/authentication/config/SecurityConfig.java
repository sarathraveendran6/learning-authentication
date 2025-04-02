package com.azure.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for non-browser-based APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/authorize", "/approve", "/login", "/authenticate", "/token").permitAll() // Allow these endpoints
                        .anyRequest().authenticated() // Secure all other endpoints
                );

        return http.build();
    }



}

