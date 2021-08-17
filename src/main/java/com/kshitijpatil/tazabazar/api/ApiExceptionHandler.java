package com.kshitijpatil.tazabazar.api;

import com.kshitijpatil.tazabazar.api.inventory.InMemoryInventoryService.InventoryNotFoundException;
import com.kshitijpatil.tazabazar.api.product.InMemoryProductService.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    private ResponseEntity<ApiErrorResponse> getResponseEntityFor(ApiError apiError, HttpStatus httpStatus) {
        var response = new ApiErrorResponse(OffsetDateTime.now(), apiError.getError(), apiError.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ProductNotFoundException ex) {
        return getResponseEntityFor(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(InventoryNotFoundException ex) {
        return getResponseEntityFor(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ValidationException ex) {
        var response = new ApiErrorResponse(OffsetDateTime.now(), "val-001", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
