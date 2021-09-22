package com.kshitijpatil.tazabazar.apiv2.product;

import com.kshitijpatil.tazabazar.apiv2.dto.InventoryOutDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductCategoryDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.lang.Nullable;
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
    private final InventoryRepository inventories;

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
    public List<ProductCategoryDto> getAllCategories() {
        return StreamSupport.stream(productCategories.findAll().spliterator(), false)
                .map(category -> new ProductCategoryDto(category.label, category.skuPrefix, category.name))
                .collect(Collectors.toList());
    }

    @Override
    public void clearAll() {
        products.deleteAll();
        productCategories.deleteAll();
    }

    @Override
    public ProductOutDto getProductBySku(String productSku) throws ProductNotFoundException {
        return products.findById(productSku)
                .map(ProductMapper::toProductOutDto)
                .orElseThrow(() -> new ProductNotFoundException(productSku));
    }

    @Override
    public List<InventoryOutDto> getProductInventoriesBySku(String productSku) throws ProductNotFoundException {
        if (!products.skuExists(productSku)) throw new ProductNotFoundException(productSku);
        return StreamSupport.stream(inventories.findAllBySku(productSku).spliterator(), false)
                .map(ProductMapper::toInventoryOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOutDto> searchProductByName(String query) {
        return toProductOutDtoList(products.searchProductByName(query));
    }

    @Override
    public List<ProductOutDto> getProductsByCategoryAndName(@Nullable String category, @Nullable String nameQuery) {
        Iterable<Product> queryResults;
        if (category != null && nameQuery != null)
            queryResults = products.searchProductByCategoryAndName(category, nameQuery);
        else if (category == null && nameQuery == null)
            queryResults = products.findAll();
        else if (nameQuery == null)
            queryResults = products.findByCategory(category);
        else
            queryResults = products.searchProductByName(nameQuery);
        return toProductOutDtoList(queryResults);
    }
}
