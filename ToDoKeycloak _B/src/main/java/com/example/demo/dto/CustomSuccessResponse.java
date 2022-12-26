package com.example.demo.dto;

import lombok.Data;

@Data
public class CustomSuccessResponse<T> {
    private final T data;

    private Integer statusCode = 0;
    private Boolean success = true;

    public CustomSuccessResponse(final T data) {
        this.data = data;
    }

}
