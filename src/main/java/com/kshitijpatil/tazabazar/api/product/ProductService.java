package com.kshitijpatil.tazabazar.api.product;

import java.util.List;

public interface ProductService {
    List<ProductOutDto> getAllProducts();

    List<Product> getProductsByCategory(ProductCategory productCategory);

    List<Product> getProductById(int productId);

    void updateProduct(Product product);

    void init();
}
