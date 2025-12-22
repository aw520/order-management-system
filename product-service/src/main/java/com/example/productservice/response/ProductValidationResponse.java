package com.example.productservice.response;

import com.example.productservice.constant.ValidationResult;

import java.util.List;

public class ProductValidationResponse {
    private ValidationResult result;
    private List<IndividualProductValidationResponse> products;
}
