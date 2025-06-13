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

    private final WebClient graphWebClient;

    public MailSubscriptionService(WebClient graphWebClient) {
        this.graphWebClient = graphWebClient;
    }

    public void fetchMailFolders() {
        graphWebClient
                .get()
                .uri("https://graph.microsoft.com/v1.0/users/YOUR_USER_ID/mailFolders")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(System.out::println)
                .block(); // For synchronous use. Prefer `.subscribe()` in reactive flows.
    }

    // Optional: automatically refresh subscription
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 2) // every 2 days
    public void renewSubscription() {
        System.out.println("🔁 Renewing Microsoft Graph subscription...");

    }
}
