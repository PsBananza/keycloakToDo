package com.example.demo.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Data
@RequiredArgsConstructor
public class Auth {
    private final String keycloak = "http://localhost:8082/api/admin";

    private final RestTemplate restTemplate = new RestTemplate();

    public String getUserToken(String token) {

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);
        String response;
        try {
            response = restTemplate.exchange(keycloak, HttpMethod.GET, entity, String.class).getBody();
        }
        catch (Exception e) {
            response = e.getMessage();
        }
        return response;
    }



}
