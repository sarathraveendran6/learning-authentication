package com.azure.authentication.controller;

import com.azure.authentication.entity.User;
import com.azure.authentication.repo.UserRepository;
import com.azure.authentication.util.ClientRegistry;
import com.azure.authentication.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
public class AuthController {

    private final Map<String, String> authCodes = new HashMap<>();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ClientRegistry clientRegistry;

    @GetMapping("/authorize")
    public ResponseEntity<String> authorize(
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam String response_type,
            @RequestParam String scope,
            HttpServletRequest request
    ) {
        // ✅ Validate response_type
        if (!"code".equals(response_type)) {
            return ResponseEntity.badRequest().body("Only response_type=code is supported.");
        }

        // ✅ Validate client_id and redirect_uri
        if (!clientRegistry.isValid(client_id, redirect_uri)) {
            return ResponseEntity.badRequest().body("Invalid client_id or redirect_uri");
        }

        // ✅ Check if user is logged in
        String user = (String) request.getSession().getAttribute("user");
        if (user == null) {
            // Not logged in → redirect to login page with original request info
            String loginUrl = "/login?client_id=" + client_id +
                    "&redirect_uri=" + redirect_uri +
                    "&response_type=" + response_type +
                    "&scope=" + scope;

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(loginUrl));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        // ✅ User is logged in → show consent screen
        String html = "<html><body>" +
                "<h2>Authorize '" + client_id + "' to access your data?</h2>" +
                "<form action='/approve' method='post'>" +
                "<input type='hidden' name='client_id' value='" + client_id + "'/>" +
                "<input type='hidden' name='redirect_uri' value='" + redirect_uri + "'/>" +
                "<button type='submit'>Approve</button>" +
                "</form></body></html>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
    }


    @PostMapping("/approve")
    public ResponseEntity<Void> approve(@RequestParam String client_id,
                                        @RequestParam String redirect_uri) {
        // ✅ Generate a random code and store it
        String code = UUID.randomUUID().toString();
        authCodes.put(code, client_id); // Later we can store user info too

        // ✅ Redirect back to client
        String redirectUrl = redirect_uri + "?code=" + code;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/token")
    public ResponseEntity<?> exchangeCodeForToken(@RequestParam String code,
                                                  @RequestParam String client_id) {
        // ✅ Check if the code exists
        if (!authCodes.containsKey(code)) {
            return ResponseEntity.badRequest().body("Invalid or expired authorization code");
        }

        // ✅ Check if the client_id matches the one associated with the code
        String storedClientId = authCodes.get(code);
        if (!storedClientId.equals(client_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Client ID mismatch");
        }

        // ✅ Generate a JWT (pretend we're issuing it to the user)
        String jwt = jwtUtil.generateToken(client_id);  // you can use real username/email later

        // ✅ Remove the code to simulate one-time use
        authCodes.remove(code);

        // ✅ Return token details in OAuth-style format
        Map<String, String> response = new HashMap<>();
        response.put("access_token", jwt);
        response.put("token_type", "Bearer");
        response.put("expires_in", "3600");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public ResponseEntity<String> showLoginPage(@RequestParam String client_id,
                                                @RequestParam String redirect_uri,
                                                @RequestParam String scope,
                                                @RequestParam String response_type,
                                                HttpServletRequest request) {

        String html = "<html><body>" +
                "<h2>Login</h2>" +
                "<form action='/authenticate' method='post'>" +
                "<input type='hidden' name='client_id' value='" + client_id + "'/>" +
                "<input type='hidden' name='redirect_uri' value='" + redirect_uri + "'/>" +
                "<input type='hidden' name='scope' value='" + scope + "'/>" +
                "<input type='hidden' name='response_type' value='" + response_type + "'/>" +
                "Username: <input type='text' name='username'/><br/>" +
                "Password: <input type='password' name='password'/><br/>" +
                "<button type='submit'>Login</button>" +
                "</form></body></html>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Void> doLogin(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String client_id,
                                        @RequestParam String redirect_uri,
                                        @RequestParam String scope,
                                        @RequestParam String response_type,
                                        HttpServletRequest request) {
        // For now, accept any non-empty credentials
        if (username != null && !username.isBlank()) {
            request.getSession().setAttribute("user", username);

            // Redirect back to /authorize with original params
            String redirect = "/authorize?client_id=" + client_id +
                    "&redirect_uri=" + redirect_uri +
                    "&response_type=" + response_type +
                    "&scope=" + scope;
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirect));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }




}
