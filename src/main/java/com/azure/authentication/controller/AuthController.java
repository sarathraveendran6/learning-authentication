package com.azure.authentication.controller;

import com.azure.authentication.entity.User;
import com.azure.authentication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Simulated session store (token -> username)
    private final Map<String, String> sessionStore = new HashMap<>();


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String sessionToken = UUID.randomUUID().toString();
                sessionStore.put(sessionToken, user.getUsername());
                return ResponseEntity.ok(sessionToken);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    @PostMapping("/items")
    public ResponseEntity<?> getItems(@RequestHeader("Authorization") String token) {
        String username = sessionStore.get(token);
        if (username != null) {
            List<String> items = Arrays.asList("Ancient Sword", "Magic Scroll", "Healing Potion");
            return ResponseEntity.ok(items);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired session token");
    }




}

