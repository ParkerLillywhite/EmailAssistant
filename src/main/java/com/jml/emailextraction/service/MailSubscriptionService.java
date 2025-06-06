package com.jml.emailextraction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class MailSubscriptionService {

    private final WebClient webClient;
    private final MicrosoftOAuth2TokenService tokenService;

    @Value("${app.notification-url}")
    private String notificationUrl;

    @Value("${app.subscription.mailbox}")
    private String mailbox;

    public MailSubscriptionService(MicrosoftOAuth2TokenService tokenService) {
        this.tokenService = tokenService;
        this.webClient = WebClient.builder()
                .baseUrl("https://graph.microsoft.com/v1.0")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // Call this on app startup or via @Scheduled task every 2.5 days
    public void registerSubscription() {
        String token = tokenService.getAccessToken();

        OffsetDateTime expirationTime = OffsetDateTime.now().plusMinutes(4200); // ~2.9 days
        String expirationTimeStr = expirationTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Map<String, Object> requestBody = Map.of(
                "changeType", "created",
                "notificationUrl", notificationUrl,
                "resource", "users/" + mailbox + "/mailFolders('Inbox')/messages",
                "expirationDateTime", expirationTimeStr,
                "clientState", "secureRandomClientValue"
        );

        webClient.post()
                .uri("/subscriptions")
                .headers(headers -> headers.setBearerAuth(token))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(response -> System.out.println("📩 Subscription created: " + response))
                .doOnError(error -> System.err.println("❌ Subscription failed: " + error.getMessage()))
                .subscribe();
    }

    // Optional: automatically refresh subscription
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 2) // every 2 days
    public void renewSubscription() {
        System.out.println("🔁 Renewing Microsoft Graph subscription...");
        registerSubscription();
    }
}
