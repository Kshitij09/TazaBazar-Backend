package com.kshitijpatil.tazabazar.api.inventory;

public interface InventoryService {
    void addInventory(Inventory inventory);

    Inventory getInventoryById(int productId);

    void updateInventory(Inventory inventory);

    void deleteInventory(int productId);
}
