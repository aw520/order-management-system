package com.ordersystem.ordermanagementsystem.exception.handler;

import com.ordersystem.ordermanagementsystem.exception.BusinessException;
import com.ordersystem.ordermanagementsystem.response.GeneralResponse;
import com.ordersystem.ordermanagementsystem.response.ServiceStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


     //Handle validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.<Void>builder()
                        .serviceStatus(ServiceStatus.builder()
                                .success(false)
                                .statusCode("VALIDATION_ERROR")
                                .message(errorMessage)
                                .build())
                        .build());
    }


     //Handle custom business exceptions
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<GeneralResponse<Void>> handleBusinessException(
            BusinessException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.<Void>builder()
                        .serviceStatus(ServiceStatus.builder()
                                .success(false)
                                .statusCode(ex.getErrorCode())
                                .message(ex.getMessage())
                                .build())
                        .build());
    }

     //Catch-all fallback (VERY important)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse<Void>> handleGenericException(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GeneralResponse.<Void>builder()
                        .serviceStatus(ServiceStatus.builder()
                                .success(false)
                                .statusCode("INTERNAL_ERROR")
                                .message("Unexpected server error")
                                .build())
                        .build());
    }
}

