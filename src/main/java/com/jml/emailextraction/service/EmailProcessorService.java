package com.jml.emailextraction.service;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import jakarta.mail.BodyPart;
import jakarta.mail.Address;
import jakarta.mail.Multipart;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.azure.identity.ClientSecretCredentialBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth.client.registration.outlook")
public class EmailProcessorService {

    private final GraphServiceClient<?> graphClient;

    private String clientId;
    private String clientSecret;
    private String tenantId;

    public EmailProcessorService(String clientId, String clientSecret, String tenantId) {
        var credential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();

        var authProvider = new TokenCredentialAuthProvider(List.of("https://graph.microsoft.com/.default"), credential);

        this.graphClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .buildClient();
    }

    public Message getMessageById(String userId, String messageId) {
        return graphClient
                .users(userId)
                .messages(messageId)
                .buildRequest()
                .get();
    }
}

