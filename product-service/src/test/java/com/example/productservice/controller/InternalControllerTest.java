package com.example.productservice.controller;

import com.example.productservice.constant.ValidationResult;
import com.example.productservice.exception.InvalidUpdateException;
import com.example.productservice.request.ProductValidationRequest;
import com.example.productservice.response.ProductValidationResponse;
import com.example.productservice.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.productservice.exception.handler.GlobalExceptionHandler;
import com.example.productservice.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InternalController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = true)
class InternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    void validateProductStock_returns200() throws Exception {

        UUID idempotencyKey = UUID.randomUUID();

        ProductValidationRequest request = new ProductValidationRequest();
        request.setIdempotencyKey(idempotencyKey.toString());
        request.setProducts(List.of());

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(ValidationResult.FULL)
                .products(List.of())
                .build();

        when(productService.validateProduct(eq(idempotencyKey), anyList()))
                .thenReturn(response);

        mockMvc.perform(post("/internal/products/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("FULL"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void validateProductStock_businessException_returns400() throws Exception {

        ProductValidationRequest request = new ProductValidationRequest();
        request.setIdempotencyKey(UUID.randomUUID().toString());
        request.setProducts(List.of());

        when(productService.validateProduct(any(), anyList()))
                .thenThrow(new InvalidUpdateException("Invalid delta"));

        mockMvc.perform(post("/internal/products/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

