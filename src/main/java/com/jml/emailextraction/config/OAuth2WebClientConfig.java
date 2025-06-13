package com.jml.emailextraction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuth2WebClientConfig {

    @Bean
    public WebClient graphWebClient(ClientRegistrationRepository clientRegistrationRepository,
                                    OAuth2AuthorizedClientRepository authorizedClientRepository) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oAuth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository);

        oAuth2.setDefaultClientRegistrationId("graph");

        return WebClient
                .builder()
                .apply(oAuth2.oauth2Configuration())
                .build();
    }
}
