package com.jml.emailextraction.config;

import com.jml.emailextraction.service.EmailProcessor;
import com.jml.emailextraction.service.MicrosoftOAuth2TokenService;
import jakarta.mail.Store;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.integration.mail.dsl.Mail;
import jakarta.mail.Session;

import java.util.Map;
import java.util.Properties;


@Configuration
@ConfigurationProperties(prefix = "spring.email")
@Getter
@Setter
public class MailIntegrationConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private Map<String, Object> properties;

//    @Bean
//    public Store imapStore(@Value("${microsoft.oauth2.access-token}") String token) {
//        Properties props = new Properties();
//        props.put("mail.store.protocol", "imap");
//        props.put("mail.imap.host", "outlook.office365.com");
//        props.put("mail.imap.port", "993");
//        props.put("mail.imap.ssl.enable", "true");
//        props.put("mail.imap.auth.mechanisms", "XOAUTH2");
//
//        Session session = Session.getInstance(props);
//
//        Store store = session.getStore("imap");
//        store.connect("outlook.office365.com", 993, "assist@rekol.me", token);
//
//        return store;
//    }
//
//    @Bean
//    public IntegrationFlow mailListenerFlow(MailReceiver mailReceiver) {
//        return IntegrationFlow
//                .from(Mail.imapIdleAdapter((ImapMailReceiver) mailReceiver)
//                        .autoStartup(true)
//                        .id("imapIdleAdapter"))
//                .handle("emailProcessor", "processMessage")
//                .get();
//    }
//
//    @Bean
//    public EmailProcessor emailProcessor() {
//        return new EmailProcessor();
//    }
}
