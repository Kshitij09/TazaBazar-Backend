package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.api.inventory.Inventory;

import java.util.List;

public interface ProductService {
    List<ProductOutDto> getAllProducts();

    List<ProductOutDto> getProductsByCategoryId(int categoryId);

    ProductOutDto getProductById(int productId);

    Inventory getInventoryById(int productId);

    void updateProduct(int productId, ProductOutDto productDto);

    void deleteProduct(int productId);

    List<CategoryDto> getAllCategories();

    void init();
}
