package com.kshitijpatil.tazabazar.apiv2.product;

public interface IProductService {
    Product saveProduct(Product product);

    ProductCategory saveProductCategory(ProductCategory productCategory);

    void clearAll();
}
