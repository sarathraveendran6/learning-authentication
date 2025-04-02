package com.sarath.client.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // ⛔ Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // ✅ Temporarily allow all (for debugging)
                );

        return http.build();
    }
}

