package com.kshitijpatil.tazabazar.api.inventory;

public class InventoryMapper {
    public static InventoryDto toInventoryDto(Inventory inventory) {
        var inventoryDto = new InventoryDto();
        inventoryDto.setId(inventory.getProductId());
        inventoryDto.setCreatedAt(inventory.getCreatedAt());
        inventoryDto.setModifiedAt(inventory.getModifiedAt());
        inventoryDto.setQuantity(inventory.getQuantity());
        return inventoryDto;
    }
}
