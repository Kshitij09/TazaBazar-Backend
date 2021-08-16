package com.kshitijpatil.tazabazar.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshitijpatil.tazabazar.api.inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

enum ProductCategory {
    VEGETABLES,
    LEAFY_VEGETABLES,
    FRUITS,
    RICE_WHEAT_ATTA,
    DALS_AND_PULSES;

    private final char[] nameDelimiters = {' ', '_'};

    String getDisplayName() {
        var name = name().replace("_", " ");
        return WordUtils.capitalizeFully(name, nameDelimiters);
    }

}

@NoArgsConstructor
@AllArgsConstructor
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
    private Inventory inventory;
    private String name;
    @JsonProperty("quantity_label")
    private String quantityLabel;
    private float price;
    @JsonProperty("image_uri")
    private String imageUri;
}