package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.utils.JsonDataSource;
import com.kshitijpatil.tazabazar.utils.MockDataFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryProductsInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private Logger logger;
    private final AtomicInteger globalProductId = new AtomicInteger(0);
    private final Map<ProductCategory, String> categoryToSkuPrefix = new HashMap<>();
    @Autowired
    private JsonDataSource dataSource;
    @Autowired
    @Qualifier("in_memory_product")
    private ProductService productService;
    @Value("${filestore.address}")
    private String fileStoreAddress;

    private int getBaseRangeFor(ProductCategory category) {
        return (category.ordinal() + 1) * 1000;
    }

    private String getImageUriFor(String originalUri) {
        var filename = originalUri.substring(originalUri.lastIndexOf("/") + 1);
        return fileStoreAddress + Paths.get("/content", filename);
    }

    private void initCategoryToSkuPrefix() {
        categoryToSkuPrefix.put(ProductCategory.FRUITS, "FR");
        categoryToSkuPrefix.put(ProductCategory.VEGETABLES, "VT");
        categoryToSkuPrefix.put(ProductCategory.LEAFY_VEGETABLES, "LVT");
        categoryToSkuPrefix.put(ProductCategory.DALS_AND_PULSES, "DP");
        categoryToSkuPrefix.put(ProductCategory.RICE_WHEAT_ATTA, "RWA");
    }


    private synchronized void storeAll(ProductCategory category, List<ProductInDto> productList) {
        int baseRange = getBaseRangeFor(category);
        for (int i = 0; i < productList.size(); i++) {
            int productId = baseRange + i + 1;
            var productSku = categoryToSkuPrefix.get(category) + productId;
            var productInDto = productList.get(i);
            var product = new Product();
            product.setProductId(globalProductId.getAndIncrement());
            product.setSku(productSku);
            product.setProductCategory(category);
            product.setName(productInDto.getName());
            product.setQuantityLabel(productInDto.getQuantityLabel());
            product.setPrice(productInDto.getPrice());
            product.setImageUri(getImageUriFor(productInDto.getImageUri()));
            // we use productId as the identifier for inventory as well
            var inventory = MockDataFactory.createInventory(product.getProductId());
            product.setProductInventory(inventory);
            productService.addProduct(product);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Initializing in-memory database");
        initCategoryToSkuPrefix();
        List<ProductInDto> productList = dataSource.getFruits();
        storeAll(ProductCategory.FRUITS, productList);
        productList = dataSource.getVegetables();
        storeAll(ProductCategory.VEGETABLES, productList);
        productList = dataSource.getLeafyVegetables();
        storeAll(ProductCategory.LEAFY_VEGETABLES, productList);
        productList = dataSource.getRiceWheatAtta();
        storeAll(ProductCategory.RICE_WHEAT_ATTA, productList);
        productList = dataSource.getDalsAndPulses();
        storeAll(ProductCategory.DALS_AND_PULSES, productList);
        logger.info("Loaded " + productService.getAllProducts().size() + " products from the fixtures");
    }
}
