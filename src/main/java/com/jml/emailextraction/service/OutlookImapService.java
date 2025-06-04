package com.jml.emailextraction.service;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.Session;
import jakarta.mail.Message;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class OutlookImapService {

    @Autowired
    private MicrosoftOAuth2TokenService tokenService;

    public void fetchEmails() throws MessagingException {
        String token = tokenService.getAccessToken();

        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", "outlook.office365.com");
        props.put("mail.imap.port", "993");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.auth.mechanisms", "XOAUTH2");

        Session session = Session.getInstance(props);
        Store store = session.getStore("imap");
        store.connect("outlook.office365.com", 993, "assist@rekol.me", token);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] messages = (Message[]) inbox.getMessages();

        for (Message message : messages) {
            System.out.println("Subject: " + message.getSubject());
        }

        inbox.close(false);
        store.close();
    }
}
