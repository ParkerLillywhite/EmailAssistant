package com.jml.emailextraction.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class MailSubscriptionService {

    private final WebClient graphWebClient;

    @Value("${graph.subscription.user-id}")
    private String userId;

    @Value("${graph.subscription.notification-url}")
    private String notificationUrl;

    public MailSubscriptionService(WebClient graphWebClient) {
        this.graphWebClient = graphWebClient;
    }

    public void registerSubscription() {
        String expirationTime = ZonedDateTime.now()
                .plusHours(1)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String requestBody = """
            {
              "changeType": "created",
              "notificationUrl": "%s",
              "resource": "/users/%s/mailFolders('Inbox')/messages",
              "expirationDateTime": "%s",
              "clientState": "secure-random-client-state"
            }
        """.formatted(notificationUrl, userId, expirationTime);

        graphWebClient.post()
                .uri("https://graph.microsoft.com/v1.0/subscriptions")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Subscription created: " + response))
                .doOnError(error -> System.err.println("Subscription error: " + error.getMessage()))
                .subscribe();
    }

    @Scheduled(fixedRate = 50 * 60 * 1000) // every 50 minutes
    public void renewSubscription() {
        registerSubscription();
    }
}
