package com.kshitijpatil.tazabazar.api.inventory;

import com.kshitijpatil.tazabazar.ApiError;
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
        inventoryMap.put(inventory.getProductId(), inventory);
    }

    @Override
    public InventoryDto getInventoryById(int productId) {
        var inventory = inventoryMap.get(productId);
        if (inventory == null) {
            throw new InventoryNotFoundException(productId);
        } else {
            return InventoryMapper.toInventoryDto(inventory);
        }
    }

    @Override
    public void updateInventory(InventoryDto inventoryDto) {
        var inventory = inventoryMap.get(inventoryDto.getId());
        if (inventory == null) {
            throw new InventoryNotFoundException(inventoryDto.getId());
        } else {
            // we're ignoring rest of the fields
            inventory.setQuantity(inventoryDto.getQuantity());
        }
    }

    @Override
    public void deleteInventory(int productId) {
        var inventory = inventoryMap.remove(productId);
        if (inventory == null)
            throw new InventoryNotFoundException(productId);
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
