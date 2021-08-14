package com.kshitijpatil.tazabazar.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshitijpatil.tazabazar.api.inventory.Inventory;
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
@Data
class CategoryDto {
    private int id;
    private String name;
}

@NoArgsConstructor
public @Data
class Product {
    private int productId;
    private ProductCategory productCategory;
    private Inventory productInventory;
    private String name;
    private String sku;
    private String quantityLabel;
    private float price;
    private String imageUri;
}

@NoArgsConstructor
@Data
class ProductOutDto {
    @JsonProperty("id")
    private int productId;
    private String sku;
    @JsonProperty("category_id")
    private int categoryId;
    @JsonProperty("inventory_id")
    private int inventoryId;
    private String name;
    @JsonProperty("quantity_label")
    private String quantityLabel;
    private float price;
    @JsonProperty("image_uri")
    private String imageUri;
}