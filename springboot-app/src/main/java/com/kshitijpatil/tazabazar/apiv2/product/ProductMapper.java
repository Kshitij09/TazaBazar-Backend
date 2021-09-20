package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.apiv2.dto.InventoryOutDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;

import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductOutDto toProductOutDto(Product product) {
        var productDto = new ProductOutDto();
        productDto.setName(product.name);
        productDto.setSku(product.sku);
        productDto.setCategory(product.category.getId());
        var inventoriesDto = product.inventories.stream()
                .map(ProductMapper::toInventoryOutDto)
                .collect(Collectors.toSet());
        productDto.setInventories(inventoriesDto);
        return productDto;
    }

    public static InventoryOutDto toInventoryOutDto(Inventory inventory) {
        var inventoryDto = new InventoryOutDto();
        inventoryDto.setId(inventory.getId());
        inventoryDto.setPrice(inventory.price);
        inventoryDto.setQuantityLabel(inventory.quantityLabel);
        inventoryDto.setUpdatedAt(inventory.updatedAt);
        inventoryDto.setStockAvailable(inventory.stockAvailable);
        return inventoryDto;
    }
}
