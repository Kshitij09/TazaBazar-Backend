package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.api.ApiErrorResponse;
import com.kshitijpatil.tazabazar.api.product.InMemoryProductService.InventoryNotFoundException;
import com.kshitijpatil.tazabazar.api.product.InMemoryProductService.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ProductExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ProductNotFoundException ex) {
        var response = new ApiErrorResponse(OffsetDateTime.now(), "pr-001", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(InventoryNotFoundException ex) {
        var response = new ApiErrorResponse(OffsetDateTime.now(), "inv-001", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
