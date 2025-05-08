package com.jml.emailextraction.controller;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.mail.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays.*;

import java.util.Properties;

@RestController
public class SendGreenMailController {

    @GetMapping("/send")
    public String sendGreenMail() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "3025");

        Session session = Session.getInstance(props);
        MimeMessage message = new MimeMessage(session);
        message.setFrom("test@localhost.com");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("assist@localhost.com"));
        message.setSubject("Hello from local");
        message.setText("This is a test email");

        Transport.send(message);
        return "success";
    }

}
