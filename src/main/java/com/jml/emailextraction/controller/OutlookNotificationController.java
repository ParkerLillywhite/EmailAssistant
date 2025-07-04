package com.jml.emailextraction.controller;

import com.jml.emailextraction.dto.NotificationPayload;
import com.jml.emailextraction.service.EmailProcessorService;
import com.jml.emailextraction.service.GraphAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.Message;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class OutlookNotificationController {

    @Autowired
    EmailProcessorService emailProcessorService;

    @GetMapping
    public ResponseEntity<String> validateNotificationUrl(@RequestParam("validationToken") String token) {
        System.out.println("Received validationToken: " + token);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(token);
    }

    @PostMapping
    public ResponseEntity<String> handleNotification(@RequestBody NotificationPayload payload) {

        payload.getValue().stream()
                .map(n -> n.getResourceData().getId())
                .forEach(emailProcessorService::processMessage);

        return ResponseEntity.ok("Received");
    }


}
