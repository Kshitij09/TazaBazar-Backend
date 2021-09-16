package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final JdbcAggregateTemplate aggregateTemplate;
    private final ProductRepository products;
    private final ProductCategoryRepository productCategories;

    @Override
    public Product saveProduct(Product product) {
        return aggregateTemplate.insert(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return IterableUtils.toList(products.findAll());
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return products.findByCategory(category);
    }

    @Override
    public ProductCategory saveProductCategory(ProductCategory productCategory) {
        return aggregateTemplate.insert(productCategory);
    }

    @Override
    public void clearAll() {
        products.deleteAll();
        productCategories.deleteAll();
    }
}
