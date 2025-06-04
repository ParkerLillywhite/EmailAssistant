package com.jml.emailextraction.config;

import com.jml.emailextraction.service.EmailProcessor;
import com.jml.emailextraction.service.MicrosoftOAuth2TokenService;
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

    @Bean
    public ImapMailReceiver imapMailReceiver(MicrosoftOAuth2TokenService tokenService,
                                             @Value("${microsoft.oauth2.user-email}") String email) {

        String accessToken = tokenService.getAccessToken();

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.imap.ssl.enable", "true");
        javaMailProperties.put("mail.imap.auth.login.disable", "true");
        javaMailProperties.put("mail.imap.auth.plain.disable", "true");
        javaMailProperties.put("mail.imap.auth.mechanisms", "XOAUTH2");

        Session session = Session.getInstance(javaMailProperties);

        String imapUrl = String.format("imaps://%s@outlook.office365.com:993/INBOX", email);

        ImapMailReceiver receiver = new ImapMailReceiver(imapUrl);
        receiver.setJavaMailProperties(javaMailProperties);
        receiver.setSession(session);
        receiver.setShouldMarkMessagesAsRead(true);
        receiver.setShouldDeleteMessages(false);
        receiver.setAutoCloseFolder(false);
        receiver.setUserFlag("spring-integration-email");

        return receiver;
    }

    @Bean
    public IntegrationFlow mailListenerFlow(MailReceiver mailReceiver) {
        return IntegrationFlow
                .from(Mail.imapIdleAdapter((ImapMailReceiver) mailReceiver)
                        .autoStartup(true)
                        .id("imapIdleAdapter"))
                .handle("emailProcessor", "processMessage")
                .get();
    }

    @Bean
    public EmailProcessor emailProcessor() {
        return new EmailProcessor();
    }
}
