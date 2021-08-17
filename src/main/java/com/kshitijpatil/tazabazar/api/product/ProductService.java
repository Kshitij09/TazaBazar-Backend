package com.kshitijpatil.tazabazar.api.product;

import java.util.List;

public interface ProductService {
    List<ProductOutDto> getAllProducts();

    List<ProductOutDto> getProductsByCategoryId(int categoryId);

    ProductOutDto getProductById(int productId);

    // Should create respective inventory as well
    void addProduct(Product product);

    void updateProduct(int productId, ProductOutDto productDto);

    void deleteProduct(int productId);

    List<CategoryDto> getAllCategories();
}
