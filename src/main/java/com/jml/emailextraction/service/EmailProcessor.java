package com.jml.emailextraction.service;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Address;
import jakarta.mail.Multipart;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EmailProcessor {

    public void processMessage(Message message) {
        try {
            Address[] froms = message.getFrom();
            String subject = message.getSubject();
            Object content = message.getContent();

            System.out.println("✅ New Email Received!");
            System.out.println("From: " + Arrays.toString(froms));
            System.out.println("Subject: " + subject);

            if (content instanceof String) {
                System.out.println("Body: " + content);
            } else if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    System.out.println("Part " + i + ": " + bodyPart.getContent());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

