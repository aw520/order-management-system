package com.example.orderservice.dto;

import com.example.orderservice.constant.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductValidationResponse {
    private ValidationResult result;
    private List<IndividualProductValidationResponse> products;
}
