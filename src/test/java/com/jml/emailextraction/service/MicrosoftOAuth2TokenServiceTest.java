package com.jml.emailextraction.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MicrosoftOAuth2TokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MicrosoftOAuth2TokenService tokenService;

    private final String TOKEN_URI = "token";
    private final String CLIENT_ID = "clientId";
    private final String CLIENT_SECRET = "clientSecret";
    private final String SCOPE = "scope";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tokenService = new MicrosoftOAuth2TokenService(restTemplate);
        tokenService.setTokenUri(TOKEN_URI);
        tokenService.setClientId(CLIENT_ID);
        tokenService.setClientSecret(CLIENT_SECRET);
        tokenService.setScope(SCOPE);
    }

    @Test
    void getAccessToken_WithAllValidValues_ReturnsAccessToken() {
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("access_token", "fake_access_token");
//
//        ResponseEntity<String> mockResponseEntity = new ResponseEntity(responseBody, HttpStatus.OK);
//        when(restTemplate.postForEntity(anyString(), any(), equals(Map.class))).thenReturn(mockResponseEntity);
//
//        String token = tokenService.getAccessToken();
//
//        Assertions.assertEquals("fake_access_token", token);
    }

    @Test
    void getAccessToken_WithInvalidTokenUri_ThrowsIllegalStateException() {

    }

    @Test
    void getAccessToken_WithInvalidClientId_ThrowsIllegalStateException() {

    }

    @Test
    void getAccessToken_WithInvalidClientSecret_ThrowsIllegalStateException() {

    }

    @Test
    void getAccessToken_WithInvalidScope_ThrowsIllegalStateException() {

    }

    @Test
    void getAccessToken_ResponseGetBodyReturnsNull_ThrowsIllegalStateException() {

    }

    @Test
    void getAccessToken_ResponseBodyPresentWithNoToken_ThrowsIllegalStateException() {

    }

}
