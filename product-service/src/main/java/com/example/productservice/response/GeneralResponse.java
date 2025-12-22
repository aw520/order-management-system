package com.example.productservice.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralResponse<T> {
    private String serviceStatus;
    private T data;
}
