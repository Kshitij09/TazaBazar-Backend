package com.kshitijpatil.tazabazar.api.inventory;

import com.kshitijpatil.tazabazar.api.ApiError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Service("in_memory_inventory")
public class InMemoryInventoryService implements InventoryService {
    volatile Map<Integer, Inventory> inventoryMap = new ConcurrentSkipListMap<>();

    @Override
    public void addInventory(Inventory inventory) {
        inventoryMap.put(inventory.getId(), inventory);
    }

    @Override
    public Inventory getInventoryById(int productId) {
        var inventory = inventoryMap.get(productId);
        if (inventory == null) {
            throw new InventoryNotFoundException(productId);
        } else {
            return inventory;
        }
    }

    @Override
    public void updateInventory(Inventory inventory) {

    }

    @Override
    public void deleteInventory(int productId) {

    }

    @AllArgsConstructor
    public static class InventoryNotFoundException extends RuntimeException implements ApiError {
        private int id;
        @Getter
        private final String error = "inv-001";

        @Override
        public String getMessage() {
            return "Inventory with id='" + id + "' not found";
        }
    }
}
