package com.example.productservice.controller;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.request.UpdateProductRequest;
import com.example.productservice.response.SpecificProductResponse;
import com.example.productservice.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.productservice.exception.handler.GlobalExceptionHandler;
import com.example.productservice.service.ProductService;
import com.example.productservice.controller.AdminController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class}) // important for exception mapping
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------
    // HAPPY PATH (ADMIN)
    // -------------------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_admin_shouldReturn200() throws Exception {
        UUID productId = UUID.randomUUID();

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Name");
        request.setQuantity(10);

        SpecificProductResponse response = SpecificProductResponse.builder()
                .id(productId.toString())
                .name("Updated Name")
                .quantity(10)
                .build();

        when(productService.updateProduct(any()))
                .thenReturn(response);

        mockMvc.perform(post("/admin/products/updateProduct/{id}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    // -------------------------
    // FORBIDDEN (NOT ADMIN)
    // -------------------------

    @Test
    @WithMockUser(roles = "USER")
    void updateProduct_nonAdmin_shouldReturn403() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(post("/admin/products/updateProduct/{id}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }

    // -------------------------
    // SERVICE EXCEPTION → HTTP ERROR
    // -------------------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_productNotFound_shouldReturn400() throws Exception {
        UUID productId = UUID.randomUUID();

        when(productService.updateProduct(any()))
                .thenThrow(new ProductNotFoundException(productId.toString()));

        mockMvc.perform(post("/admin/products/updateProduct/{id}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

