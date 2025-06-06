package com.jml.emailextraction.service;

import lombok.Setter;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@ConfigurationProperties(prefix = "microsoft.oauth2")
@Setter
public class MicrosoftOAuth2TokenService {
    private final RestTemplate restTemplate;

    private String tokenUri;
    private String clientId;
    private String clientSecret;
    private String scope;

    private String refreshToken;

    @Autowired
    public MicrosoftOAuth2TokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);


        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null || !responseBody.containsKey("access_token")) {
            throw new IllegalStateException("Access token could not be retrieved.");
        }

        // refresh token gathered here if it exists
        if(responseBody.containsKey("refresh_token)")) {
            refreshToken = responseBody.get("refresh_token").toString();
            // TODO repository.save(refreshtoken) add a scheduled check token to check token expiry and proactively refresh? ***Shouldn't be necessary with Spring Oauth2
        }

        return (String) responseBody.get("access_token");
    }
}

