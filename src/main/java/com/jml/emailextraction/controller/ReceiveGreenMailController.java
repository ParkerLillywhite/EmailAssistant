package com.jml.emailextraction.controller;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.mail.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiveGreenMailController {

    @GetMapping("/getEmails")
    public String getEmails() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", "localhost");
        props.put("mail.imap.port", "3143");

        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imap");
        store.connect("assist@localhost.com", "password");

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] messages = inbox.getMessages();

        for (Message message : messages) {
            System.out.println("Received: " + message.getSubject());
        }
        return "read emails";
    }
}
