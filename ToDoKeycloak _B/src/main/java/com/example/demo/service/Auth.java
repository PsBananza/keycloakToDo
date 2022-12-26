package com.example.demo.service;

import com.example.demo.custom.TokenMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Data
@RequiredArgsConstructor
public class Auth {

//    @Value("keycloak.auth-server-url")
//    private final String keycloak = "http://localhost:8080/realms/SpringBootCloak/protocol/openid-connect/token";

    private final RestTemplate restTemplate = new RestTemplate();

    public String getUserToken() {


        return null;
    }



}
