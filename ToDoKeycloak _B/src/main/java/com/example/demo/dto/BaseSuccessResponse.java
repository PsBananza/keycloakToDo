package com.example.demo.dto;

import lombok.Data;

@Data
public class BaseSuccessResponse {

    Integer statusCode = 200;
    Boolean success = true;

}
