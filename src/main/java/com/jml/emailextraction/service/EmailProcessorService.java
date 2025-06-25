package com.jml.emailextraction.service;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.jml.emailextraction.config.OutlookOAuth2Properties;
import com.jml.emailextraction.dto.MessageDto;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.models.Message;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class EmailProcessorService {

    private final WebClient graphWebClient;

    public EmailProcessorService(OutlookOAuth2Properties props, WebClient graphWebClient) {
        var credential = new ClientSecretCredentialBuilder()
                .clientId(props.getClientId())
                .clientSecret(props.getClientSecret())
                .tenantId(props.getTenantId())
                .build();

        var authProvider = new TokenCredentialAuthProvider(List.of("https://graph.microsoft.com/.default"), credential);

        this.graphWebClient = graphWebClient;
    }

//    public Message getMessageById(String userId, String messageId) {
//        return graphWebClient
//                .get()
//                .uri("https://graph.microsoft.com/v1.0/users/{userId}/messages/{messageId}", userId, messageId)
//                .retrieve()
//                .bodyToMono(MessageDto.class);
//        return new Message();
//    }

    public void processMessage(String messageId) {
        try {
            graphWebClient
                    .get()
                    .uri("https://graph.microsoft.com/v1.0/me/messages/{messageId}", messageId)
                    .retrieve()
                    .bodyToMono(MessageDto.class)
                    .doOnNext(message -> {
                        System.out.println("Subject: " + message.getSubject());
                        System.out.println("From: " + message.getFrom().getEmailAddress().getAddress());
                        System.out.println("Body preview: " + message.getFrom().getEmailAddress().getAddress());
                        System.out.println("Body content: " + message.getBody().getContent());
                    })
                    .doOnError(Throwable::printStackTrace)
                    .subscribe();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

