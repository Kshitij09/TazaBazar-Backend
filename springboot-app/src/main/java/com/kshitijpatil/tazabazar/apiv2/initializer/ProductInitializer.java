package com.kshitijpatil.tazabazar.apiv2.initializer;

import com.kshitijpatil.tazabazar.api.product.ProductInDto;
import com.kshitijpatil.tazabazar.apiv2.product.IProductService;
import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import com.kshitijpatil.tazabazar.apiv2.product.Product;
import com.kshitijpatil.tazabazar.apiv2.product.ProductCategory;
import com.kshitijpatil.tazabazar.utils.JsonDataSource;
import com.kshitijpatil.tazabazar.utils.MockDataFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@Slf4j
public class ProductInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final JsonDataSource jsonDataSource;
    private final IProductService productService;
    private final Map<String, Supplier<List<ProductInDto>>> productSupplier = new HashMap<>();
    @Value("${filestore.address}")
    private String fileStoreAddress;

    private String getImageUriFor(String originalUri) {
        var filename = originalUri.substring(originalUri.lastIndexOf("/") + 1);
        return String.format("http://%s/content/%s", fileStoreAddress, filename);
    }

    public ProductInitializer(JsonDataSource jsonDataSource, IProductService productService) {
        this.jsonDataSource = jsonDataSource;
        this.productService = productService;

        productSupplier.put("vegetables", jsonDataSource::getVegetables);
        productSupplier.put("fruits", jsonDataSource::getFruits);
        productSupplier.put("leafy-vegetables", jsonDataSource::getLeafyVegetables);
        productSupplier.put("dals-and-pulses", jsonDataSource::getDalsAndPulses);
        productSupplier.put("rice-wheat-atta", jsonDataSource::getRiceWheatAtta);
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Deleting database entries from Product aggregate");
        productService.clearAll();
        log.info("Initializing Product aggregate");
        loadProductsByCategory();
    }

    private void loadProductsByCategory() {
        jsonDataSource.getProductCategories()
                .forEach(categoryDto -> {
                    var productCategory = new ProductCategory(categoryDto.label,
                            categoryDto.skuPrefix,
                            categoryDto.name);
                    productCategory = productService.saveProductCategory(productCategory);
                    loadProductsFor(productCategory);
                });
    }

    private void loadProductsFor(ProductCategory category) {
        var productInDtos = productSupplier.get(category.label).get();
        for (int idx = 0; idx < productInDtos.size(); idx++) {
            var productDto = productInDtos.get(idx);
            var nextProductId = String.format("%03d", idx + 1);
            var productSku = String.format("%s-%s", category.skuPrefix, nextProductId);
            var product = new Product(productSku, productDto.getName(), AggregateReference.to(category.label));
            product.imageUri = getImageUriFor(productDto.getImageUri());
            var defaultInventory = makeDefaultInventoryFrom(productDto);
            product.add(defaultInventory);
            productService.saveProduct(product);
        }
        log.info(String.format("Added %d products for category: %s", productInDtos.size(), category.label));
    }

    private Inventory makeDefaultInventoryFrom(ProductInDto product) {
        var availableStock = MockDataFactory.getRandomStock(100, 80);
        return new Inventory(product.getQuantityLabel(),
                product.getPrice(),
                Instant.now(),
                availableStock);
    }
}
