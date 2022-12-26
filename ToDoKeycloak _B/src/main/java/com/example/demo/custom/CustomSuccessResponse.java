package com.example.demo.custom;

import lombok.Data;

@Data
public class CustomSuccessResponse<T> {
    final T data;

    Integer statusCode = 0;
    Boolean success = true;

    public CustomSuccessResponse(T data) {
        this.data = data;
    }

}
