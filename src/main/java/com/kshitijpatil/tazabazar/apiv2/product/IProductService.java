package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.apiv2.dto.ProductCategoryDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;

import java.util.List;

public interface IProductService {
    Product saveProduct(Product product);

    List<ProductOutDto> getAllProducts();

    List<ProductOutDto> getProductsByCategory(String category);

    ProductCategory saveProductCategory(ProductCategory productCategory);

    List<ProductCategoryDto> getAllCategories();

    void clearAll();
}
