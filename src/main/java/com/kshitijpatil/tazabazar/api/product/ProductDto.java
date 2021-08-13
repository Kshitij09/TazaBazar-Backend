package com.kshitijpatil.tazabazar.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class ProductDto {
    private String name;
    @JsonProperty("quantity")
    private String quantityLabel;
    private float price;
    @JsonProperty("image_uri")
    private String imageUri;
}