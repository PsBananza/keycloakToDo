package com.example.demo.service;

import com.example.demo.custom.RegisterUser;
import com.example.demo.custom.User;
import com.example.demo.dto.UserLoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
public interface AuthService {
    String createUser(RegisterUser user) throws JsonProcessingException;
    String get(String token);
    UserLoginDto getAccessToken(User user);
}
