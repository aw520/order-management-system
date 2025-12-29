package com.example.productservice.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class GeneralResponse<T> {
    private String serviceStatus;
    private T data;
}
