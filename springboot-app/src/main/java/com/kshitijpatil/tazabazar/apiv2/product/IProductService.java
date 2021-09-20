package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.apiv2.dto.InventoryOutDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductCategoryDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IProductService {
    Product saveProduct(Product product);

    List<ProductOutDto> getAllProducts();

    List<ProductOutDto> getProductsByCategory(String category);

    ProductCategory saveProductCategory(ProductCategory productCategory);

    List<ProductCategoryDto> getAllCategories();

    void clearAll();

    ProductOutDto getProductBySku(String productSku) throws ProductNotFoundException;

    List<InventoryOutDto> getProductInventoriesBySku(String productSku) throws ProductNotFoundException;

    List<ProductOutDto> searchProductByName(String query);

    List<ProductOutDto> getProductsByCategoryAndName(@Nullable String category, @Nullable String nameQuery);
}
