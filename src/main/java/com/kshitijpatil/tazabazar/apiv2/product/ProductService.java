package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<ProductOutDto> getAllProducts() {
        return toProductOutDtoList(products.findAll());
    }

    private List<ProductOutDto> toProductOutDtoList(Iterable<Product> products) {
        return StreamSupport.stream(products.spliterator(), false)
                .map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOutDto> getProductsByCategory(String category) {
        return toProductOutDtoList(products.findByCategory(category));
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
