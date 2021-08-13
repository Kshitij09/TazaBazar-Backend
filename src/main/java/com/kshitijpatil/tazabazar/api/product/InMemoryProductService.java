package com.kshitijpatil.tazabazar.api.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Service("in_memory")
public class InMemoryProductService implements ProductService {
    private final JsonDataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(InMemoryProductService.class);
    volatile Map<Integer, Product> productsMap = new ConcurrentSkipListMap<>();

    @Autowired
    public InMemoryProductService(JsonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private int getBaseRangeFor(ProductCategory category) {
        return (category.ordinal() + 1) * 1000;
    }

    private synchronized void storeAll(ProductCategory category, List<ProductDto> productList) {
        int baseRange = getBaseRangeFor(category);
        for (int i = 0; i < productList.size(); i++) {
            int productId = baseRange + i + 1;
            var productDto = productList.get(i);
            var product = new Product();
            product.setProductId(productId);
            product.setProductCategory(category);
            product.setName(productDto.getName());
            product.setQuantityLabel(productDto.getQuantityLabel());
            product.setPrice(productDto.getPrice());
            product.setImageUri(product.getImageUri());
            productsMap.put(productId, product);
        }
    }

    @Override
    public synchronized void init() {
        logger.info("Initializing in-memory database");
        List<ProductDto> productList = dataSource.getFruits();
        storeAll(ProductCategory.FRUITS, productList);
        productList = dataSource.getVegetables();
        storeAll(ProductCategory.VEGETABLES, productList);
        productList = dataSource.getLeafyVegetables();
        storeAll(ProductCategory.LEAFY_VEGETABLES, productList);
        productList = dataSource.getRiceWheatAtta();
        storeAll(ProductCategory.RICE_WHEAT_ATTA, productList);
        productList = dataSource.getDalsAndPulses();
        storeAll(ProductCategory.DALS_AND_PULSES, productList);
        logger.info("Loaded " + productsMap.size() + " products from fixtures");
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productsMap.values());
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory productCategory) {
        return null;
    }

    @Override
    public List<Product> getProductById(int productId) {
        return null;
    }

    @Override
    public void updateProduct(Product product) {

    }

}
