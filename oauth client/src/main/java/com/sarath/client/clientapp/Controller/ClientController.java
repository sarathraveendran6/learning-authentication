package com.sarath.client.clientapp.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
public class ClientController {

    @Value("${auth.server.url}")
    private String authServer;

    @Value("${client.id}")
    private String clientId;

    @Value("${redirect.uri}")
    private String redirectUri;

    private String accessToken; // Just storing in memory for now

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String url = authServer + "/authorize" +
                "?response_type=code" +
                "&client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                "&scope=read_items";

        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Exchange code for token
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(authServer + "/token", request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String accessToken = (String) response.getBody().get("access_token");
            model.addAttribute("token", accessToken);

            // Step 2: Call Resource Server using the token
            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> resourceRequest = new HttpEntity<>(authHeaders);

            ResponseEntity<List> itemResponse = restTemplate.exchange(
                    "http://localhost:8082/items",
                    HttpMethod.GET,
                    resourceRequest,
                    List.class
            );

            if (itemResponse.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("items", itemResponse.getBody());
            } else {
                model.addAttribute("error", "Failed to fetch items from resource server");
            }

        } else {
            model.addAttribute("error", "Token exchange failed");
        }

        return "callback";
    }

}
