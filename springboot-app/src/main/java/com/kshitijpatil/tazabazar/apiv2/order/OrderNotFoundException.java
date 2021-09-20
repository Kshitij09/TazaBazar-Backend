package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderNotFoundException extends RuntimeException implements ApiError {
    @Getter
    public final String error = "order-001";
    public final String orderId;

    @Override
    public String getMessage() {
        return String.format("Order with id='%s' not found", orderId);
    }
}
