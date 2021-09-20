package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryNotFoundException extends RuntimeException implements ApiError {
    private final long id;
    @Getter
    private final String error = "inv-001";

    @Override
    public String getMessage() {
        return "Inventory with id='" + id + "' not found";
    }
}
