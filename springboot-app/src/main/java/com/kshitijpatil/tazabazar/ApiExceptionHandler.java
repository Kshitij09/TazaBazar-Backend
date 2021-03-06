package com.kshitijpatil.tazabazar;

import com.kshitijpatil.tazabazar.api.inventory.InMemoryInventoryService.InventoryNotFoundException;
import com.kshitijpatil.tazabazar.api.product.InMemoryProductService.ProductNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.userauth.RoleNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.userdetail.PhoneExistsException;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UserNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UsernameExistsException;
import com.kshitijpatil.tazabazar.security.jwt.RefreshTokenNotFoundException;
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

    @ExceptionHandler(com.kshitijpatil.tazabazar.apiv2.product.ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(com.kshitijpatil.tazabazar.apiv2.product.ProductNotFoundException ex) {
        return getResponseEntityFor(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(UserNotFoundException ex) {
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

    @ExceptionHandler(PhoneExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(PhoneExistsException ex) {
        return getResponseEntityFor(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(UsernameExistsException ex) {
        return getResponseEntityFor(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleApiException(RoleNotFoundException ex) {
        return getResponseEntityFor(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleApiException(com.kshitijpatil.tazabazar.apiv2.product.InventoryNotFoundException ex) {
        return getResponseEntityFor(ex, HttpStatus.NOT_FOUND);
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
