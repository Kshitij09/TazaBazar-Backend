package com.kshitijpatil.tazabazar.api;

import com.kshitijpatil.tazabazar.api.inventory.InMemoryInventoryService.InventoryNotFoundException;
import com.kshitijpatil.tazabazar.api.product.InMemoryProductService.ProductNotFoundException;
import com.kshitijpatil.tazabazar.api.security.jwt.RefreshTokenNotFoundException;
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
        return new ResponseEntity<>(response, httpStatus);
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
        return getResponseEntityFor(toApiError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(RefreshTokenNotFoundException ex) {
        return getResponseEntityFor(ex, HttpStatus.FORBIDDEN);
    }

    private ApiError toApiError(ValidationException exception) {
        final String DEFAULT_ERROR_CODE = "val-001";
        return new ApiError() {
            @Override
            public String getMessage() {
                return exception.getMessage();
            }

            @Override
            public String getError() {
                return DEFAULT_ERROR_CODE;
            }
        };
    }
}
