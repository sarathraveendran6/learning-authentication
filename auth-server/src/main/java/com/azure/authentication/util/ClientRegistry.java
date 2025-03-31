package com.azure.authentication.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "clients")
public class ClientRegistry {

    private List<Client> list = new ArrayList<>();

    public List<Client> getList() {
        return list;
    }

    public void setList(List<Client> list) {
        this.list = list;
    }

    public static class Client {
        private String client_id;
        private String redirect_uri;

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getRedirect_uri() {
            return redirect_uri;
        }

        public void setRedirect_uri(String redirect_uri) {
            this.redirect_uri = redirect_uri;
        }
    }

    public boolean isValid(String clientId, String redirectUri) {
        return list.stream().anyMatch(client ->
                client.getClient_id().equals(clientId) &&
                        client.getRedirect_uri().equals(redirectUri)
        );
    }


}

