package com.jml.emailextraction.controller;

import com.jml.emailextraction.service.MailSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/subscribe")
public class GraphSubscriptionController {

    private final MailSubscriptionService emailSubscriptionService;

    public GraphSubscriptionController(MailSubscriptionService emailSubscriptionService) {
        this.emailSubscriptionService = emailSubscriptionService;
    }

    @PostMapping
    public ResponseEntity<String> subscribe() {
        try {
            emailSubscriptionService.registerSubscription();  // fire-and-forget
            return ResponseEntity.ok("Subscription request sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send subscription: " + e.getMessage());
        }
    }
}
