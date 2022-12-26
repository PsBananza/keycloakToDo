package com.example.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetNewsDto<T> {
    private List<T> content = List.of();
    private int notReady;
    private int numberOfElements;
    private int ready;

}
