package com.kshitijpatil.tazabazar.api.inventory;

public interface InventoryService {
    void addInventory(Inventory inventory);

    InventoryDto getInventoryById(int productId);

    void updateInventory(InventoryDto inventoryDto);

    void deleteInventory(int productId);
}
