package com.example.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserLoginDto {
    String token;
    String role;
}
