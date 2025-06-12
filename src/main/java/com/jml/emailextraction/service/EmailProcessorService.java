package com.jml.emailextraction.service;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.jml.emailextraction.config.OutlookOAuth2Properties;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.models.Message;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailProcessorService {

    private final GraphServiceClient<?> graphClient;

    public EmailProcessorService(OutlookOAuth2Properties props) {
        var credential = new ClientSecretCredentialBuilder()
                .clientId(props.getClientId())
                .clientSecret(props.getClientSecret())
                .tenantId(props.getTenantId())
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

    public void processMessage(String messageId) {
        System.out.println(messageId);
    }
}

