package com.jml.emailextraction.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth.client.registration.outlook")
@RequiredArgsConstructor
@Getter
public class OutlookOAuth2Properties {
    private final String clientId;
    private final String clientSecret;
    private final String tenantId;

}
