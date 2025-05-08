package com.jml.emailextraction.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GreenMail greenMailServer() {
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
        greenMail.setUser("assist@localhost.com", "password");
        return greenMail;
    }

}
