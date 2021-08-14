package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.api.inventory.Inventory;
import com.kshitijpatil.tazabazar.api.utils.MockDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("in_memory")
public class InMemoryProductService implements ProductService {
    private final JsonDataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(InMemoryProductService.class);
    volatile Map<Integer, Product> productsMap = new ConcurrentSkipListMap<>();
    private final Map<ProductCategory, String> categoryToSkuPrefix = new HashMap<>();
    volatile Map<Integer, Inventory> productInventoryMap = new ConcurrentSkipListMap<>();
    private AtomicInteger globalProductId = new AtomicInteger(0);

    @Value("${filestore.base-url}")
    private String fileStoreBaseUrl;

    private void initCategoryToSkuPrefix() {
        categoryToSkuPrefix.put(ProductCategory.FRUITS, "FR");
        categoryToSkuPrefix.put(ProductCategory.VEGETABLES, "VT");
        categoryToSkuPrefix.put(ProductCategory.LEAFY_VEGETABLES, "LVT");
        categoryToSkuPrefix.put(ProductCategory.DALS_AND_PULSES, "DP");
        categoryToSkuPrefix.put(ProductCategory.RICE_WHEAT_ATTA, "RWA");
    }

    @Autowired
    public InMemoryProductService(JsonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private int getBaseRangeFor(ProductCategory category) {
        return (category.ordinal() + 1) * 1000;
    }

    private String getImageUriFor(String originalUri) {
        var filename = originalUri.substring(originalUri.lastIndexOf("/") + 1);
        return fileStoreBaseUrl + filename;
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
            var inventory = MockDataFactory.createInventory(product.getProductId());
            product.setProductInventory(inventory);
            productsMap.put(productId, product);
            productInventoryMap.put(productId, inventory);
        }
    }

    @Override
    public synchronized void init() {
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
        logger.info("Loaded " + productsMap.size() + " products from fixtures");
    }

    @Override
    public List<ProductOutDto> getAllProducts() {
        var allProducts = productsMap.values();
        return allProducts.stream()
                .map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
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
