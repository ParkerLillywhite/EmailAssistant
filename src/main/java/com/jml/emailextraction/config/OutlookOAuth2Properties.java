package com.jml.emailextraction.config;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.outlook")
@Data
public class OutlookOAuth2Properties {
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private String scope;
    private String grantType;
    private String provider;
    private String tokenUri;
}
