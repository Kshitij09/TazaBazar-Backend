package com.kshitijpatil.tazabazar.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

enum ProductCategory {
    VEGETABLES,
    LEAFY_VEGETABLES,
    FRUITS,
    RICE_WHEAT_ATTA,
    DALS_AND_PULSES
}

@NoArgsConstructor
public @Data
class Product {
    private int productId;
    private ProductCategory productCategory;
    private String name;
    @JsonProperty("quantity")
    private String quantityLabel;
    private float price;
    @JsonProperty("image_uri")
    private String imageUri;
}
