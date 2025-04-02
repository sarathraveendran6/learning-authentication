package com.azure.authentication;

import com.azure.authentication.util.ClientRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(ClientRegistry.class)

public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> logClients(ClientRegistry clientRegistry) {
        return event -> {
            System.out.println("✅ Loaded registered clients:");
            clientRegistry.getList().forEach(client -> {
                System.out.println("  - client_id: " + client.getClient_id());
                System.out.println("    redirect_uri: " + client.getRedirect_uri());
            });
        };
    }

}
