package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.api.inventory.InventoryService;
import com.kshitijpatil.tazabazar.api.utils.MockDataFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("in_memory_product")
public class InMemoryProductService implements ProductService {
    @Autowired
    private JsonDataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(InMemoryProductService.class);
    volatile Map<Integer, Product> productsMap = new ConcurrentSkipListMap<>();
    private final Map<ProductCategory, String> categoryToSkuPrefix = new HashMap<>();
    private final AtomicInteger globalProductId = new AtomicInteger(0);
    private final ProductCategory[] categoryValues = ProductCategory.values();

    @Autowired
    @Qualifier("in_memory_inventory")
    private InventoryService inventoryService;

    @Value("${filestore.base-url}")
    private String fileStoreBaseUrl;

    private void initCategoryToSkuPrefix() {
        categoryToSkuPrefix.put(ProductCategory.FRUITS, "FR");
        categoryToSkuPrefix.put(ProductCategory.VEGETABLES, "VT");
        categoryToSkuPrefix.put(ProductCategory.LEAFY_VEGETABLES, "LVT");
        categoryToSkuPrefix.put(ProductCategory.DALS_AND_PULSES, "DP");
        categoryToSkuPrefix.put(ProductCategory.RICE_WHEAT_ATTA, "RWA");
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
            // we use productId as the identifier for inventory as well
            var inventory = MockDataFactory.createInventory(product.getProductId());
            product.setProductInventory(inventory);
            productsMap.put(product.getProductId(), product);
            inventoryService.addInventory(inventory);
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

    // note: https://stackoverflow.com/a/609866/6738702
    @Override
    public List<ProductOutDto> getAllProducts() {
        var allProducts = productsMap.values();
        return allProducts.stream()
                .map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOutDto> getProductsByCategoryId(int categoryId) {
        var allProducts = productsMap.values();
        return allProducts.stream()
                .filter(product -> product.getProductCategory() == categoryValues[categoryId])
                .map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductOutDto getProductById(int productId) {
        var product = productsMap.get(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        } else {
            return ProductMapper.toProductOutDto(product);
        }
    }

    @Override
    public void updateProduct(int productId, ProductOutDto productDto) {
        if (productsMap.containsKey(productId)) {
            var product = ProductMapper.fromProductDto(productDto);
            productsMap.put(productId, product);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        var product = productsMap.remove(productId);
        if (product == null) {
            // item not found
            throw new ProductNotFoundException(productId);
        } else {
            inventoryService.deleteInventory(productId);
        }
    }

    private CategoryDto toCategoryDto(ProductCategory category) {
        return new CategoryDto(category.ordinal(), category.getDisplayName());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return Arrays
                .stream(ProductCategory.values())
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    public static class ProductNotFoundException extends RuntimeException {
        private int id;

        @Override
        public String getMessage() {
            return "Product with id='" + id + "' not found";
        }
    }

}
