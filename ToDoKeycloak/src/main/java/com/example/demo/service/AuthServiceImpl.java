package com.example.demo.service;

import com.example.demo.custom.KeycloakAuthResponse;
import com.example.demo.custom.RegisterUser;
import com.example.demo.custom.User;
import com.example.demo.dto.UserLoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceImpl implements AuthService{

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String SCOPE = "openid";
    private static final String OPENID = "openid";
    private static final String authUrl = "http://localhost:8080/realms/master/protocol/openid-connect/token";
    private static final String admUrl = "http://localhost:8080/realms/master/protocol/openid-connect/token";
    private static final String regUrl = "http://localhost:8080/realms/master/users";
    private static final String serviceClient = "login-app";
    private static final String serviceSecret = "WZ9AiOgQXOyUTXF6WGKrsF5ZAekxjrGm";
    private static final String testUrl = "http://localhost:8082/api/get";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String role = "admin";
    private static final String adminName = "admin";
    private static final String adminPassword = "admin";
    private static final String realmAdmin = "master";
    private static final String adminClientId = "admin-cli";
    @Override
    public String createUser(RegisterUser user) throws JsonProcessingException {
        ResponseEntity<KeycloakAuthResponse> response = null;

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(CLIENT_ID, "login-app");
        paramMap.add(GRANT_TYPE, PASSWORD);
        paramMap.add(SCOPE, OPENID);
        paramMap.add(USERNAME, adminName);
        paramMap.add(PASSWORD, "admin");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);
        try {
            response = restTemplate.exchange(admUrl, HttpMethod.POST, entity, KeycloakAuthResponse.class);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.setBearerAuth(response.getBody().getAccessToken());
        String clientBody = mapper.writeValueAsString(user);
        try {
            response = restTemplate.exchange(regUrl, HttpMethod.POST, new HttpEntity(clientBody, clientHeaders), KeycloakAuthResponse.class);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "User created";
    }

    @Override
    public UserLoginDto getAccessToken(User user){
        AuthzClient authzClient = AuthzClient.create();

// create an authorization request
        AuthorizationRequest request = new AuthorizationRequest();

// add permissions to the request based on the resources and scopes you want to check access
        request.getClaimToken();

// send the entitlement request to the server in order to
// obtain an RPT with permissions for a single resource
        String response = authzClient.obtainAccessToken(user.getUsername(), user.getPassword()).getToken();
//        String rpt = response.getToken();
        UserLoginDto userLoginDto = new UserLoginDto().setToken(response);
        return userLoginDto;
    }

    @Override
    public String get(String token){
        String response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        try {
            response = restTemplate.exchange(testUrl, HttpMethod.GET, new HttpEntity(headers), String.class).getBody();
        }
        catch (Exception e) {
            response = e.getMessage();
        }
        return response;
    }
}
