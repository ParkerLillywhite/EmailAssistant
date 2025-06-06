package com.jml.emailextraction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class OutlookNotificationController {

    @PostMapping
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> payload,
                                                     @RequestHeader(value = "validationToken", required = false) String validationToken) {
        // Validate the webhook if Microsoft is testing it
        if (validationToken != null) {
            return ResponseEntity.ok(validationToken);
        }

        System.out.println("Received new email notification: " + payload);
        // Optionally: Fetch full email details using Graph API here
        return ResponseEntity.ok("Received");
    }
}
