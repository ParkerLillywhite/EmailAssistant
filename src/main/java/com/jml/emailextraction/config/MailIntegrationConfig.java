package com.jml.emailextraction.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


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
