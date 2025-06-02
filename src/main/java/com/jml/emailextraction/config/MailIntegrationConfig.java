package com.jml.emailextraction.config;

import com.jml.emailextraction.service.EmailProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.integration.mail.dsl.Mail;


@Configuration
@ConfigurationProperties(prefix = "email")
public class MailIntegrationConfig {

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private int port;

    @Bean
    public MailReceiver imapMailReceiver() {
        String imapUrl = String.format("imap://%s:%s@%s:%d/INBOX", username, password, host, port);
        ImapMailReceiver receiver = new ImapMailReceiver(imapUrl);
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
