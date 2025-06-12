package com.jml.emailextraction.controller;

import com.jml.emailextraction.service.EmailProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> payload,
                                                     @RequestHeader(value = "validationToken", required = false) String validationToken) {

        List<Map<String, Object>> value = (List<Map<String, Object>>) payload.get("value"); // look up Unchecked class?

        for (Map<String, Object> notification : value) {
            Map<String, Object> resourceData = (Map<String, Object>) notification.get("resourceData");
            String messageId = (String) resourceData.get("id");

            emailProcessorService.processMessage(messageId);
        }

        System.out.println("Received new email notification: " + payload);
        // Optionally: Fetch full email details using Graph API here
        return ResponseEntity.ok("Received");
    }
}
