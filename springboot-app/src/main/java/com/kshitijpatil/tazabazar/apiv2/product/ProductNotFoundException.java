package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductNotFoundException extends RuntimeException implements ApiError {
    @Getter
    public final String error = "product-001";
    private final String productSku;

    @Override
    public String getMessage() {
        return String.format("Product with product_sku='%s' not found", productSku);
    }
}
