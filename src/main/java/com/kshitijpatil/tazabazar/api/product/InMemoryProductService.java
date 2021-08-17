package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.api.ApiError;
import com.kshitijpatil.tazabazar.api.inventory.InventoryMapper;
import com.kshitijpatil.tazabazar.api.inventory.InventoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@Service("in_memory_product")
public class InMemoryProductService implements ProductService {
    volatile Map<Integer, Product> productsMap = new ConcurrentSkipListMap<>();
    private final ProductCategory[] categoryValues = ProductCategory.values();

    @Autowired
    @Qualifier("in_memory_inventory")
    private InventoryService inventoryService;


    // note: https://stackoverflow.com/a/609866/6738702
    @Override
    public List<ProductOutDto> getAllProducts() {
        var allProducts = productsMap.values();
        return allProducts.stream()
                .map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOutDto> getProductsByCategoryId(int categoryId) {
        var allProducts = productsMap.values();
        return allProducts.stream()
                .filter(product -> product.getProductCategory() == categoryValues[categoryId])
                .map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductOutDto getProductById(int productId) {
        var product = productsMap.get(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        } else {
            return ProductMapper.toProductOutDto(product);
        }
    }

    @Override
    public void addProduct(Product product) {
        productsMap.put(product.getProductId(), product);
        inventoryService.addInventory(product.getProductInventory());
    }

    @Override
    public void updateProduct(int productId, ProductOutDto productDto) {
        var product = productsMap.get(productId);
        if (product != null) {
            var productUpdates = ProductMapper.fromProductDto(productDto);
            // Ignoring productId and sku updates
            // Inventory updates are considered
            // maintaining the consistency
            product.setProductCategory(productUpdates.getProductCategory());
            product.setPrice(productUpdates.getPrice());
            product.setName(productUpdates.getName());
            product.setQuantityLabel(productUpdates.getQuantityLabel());
            product.setImageUri(productUpdates.getImageUri());
            inventoryService.updateInventory(InventoryMapper.toInventoryDto(productUpdates.getProductInventory()));
            product.setProductInventory(productUpdates.getProductInventory());
            productsMap.put(productId, product);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        var product = productsMap.remove(productId);
        if (product == null) {
            // item not found
            throw new ProductNotFoundException(productId);
        } else {
            inventoryService.deleteInventory(productId);
        }
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return Arrays
                .stream(ProductCategory.values())
                .map(CategoryDto::fromProductCategory)
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    public static class ProductNotFoundException extends RuntimeException implements ApiError {
        private int id;
        @Getter
        private final String error = "pr-001";

        @Override
        public String getMessage() {
            return "Product with id='" + id + "' not found";
        }
    }

}
