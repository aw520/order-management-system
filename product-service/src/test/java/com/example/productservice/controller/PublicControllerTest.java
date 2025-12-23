package com.example.productservice.controller;

import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.exception.handler.GlobalExceptionHandler;
import com.example.productservice.response.GeneralSearchProductResponse;
import com.example.productservice.response.SpecificProductResponse;
import com.example.productservice.security.SecurityConfig;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PublicController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = true) // harmless even if no security
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------
    // GET /products/{id}
    // -------------------------

    @Test
    @WithAnonymousUser
    void searchProductById_returns200() throws Exception {
        UUID productId = UUID.randomUUID();

        SpecificProductResponse response = SpecificProductResponse.builder()
                .id(productId.toString())
                .name("Test Product")
                .quantity(5)
                .build();

        when(productService.searchProductById(productId))
                .thenReturn(response);

        mockMvc.perform(get("/products/{id}", productId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void searchProductById_invalidUUID_returns400() throws Exception {
        mockMvc.perform(get("/products/{id}", "not-a-uuid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    // -------------------------
    // GET /products
    // -------------------------

    @Test
    void searchProduct_returns200() throws Exception {
        GeneralSearchProductResponse product =
                GeneralSearchProductResponse.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Laptop")
                        .quantity(10)
                        .build();

        when(productService.searchProduct(any(SearchCriteria.class)))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("keyword", "lap")
                        .param("inStock", "true")
                        .param("sortBy", "productName")
                        .param("sortDirection", "1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }
}

