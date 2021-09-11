package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.apiv2.product.*;
import com.kshitijpatil.tazabazar.util.TestPostgreConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestPostgreConfig.class)
@EnableJdbcRepositories
@Sql("classpath:schema.sql")
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    private ProductRepository products;
    @Autowired
    private ProductCategoryRepository productCategories;

    @Autowired
    private InventoryRepository inventories;

    private ProductCategory insertVegetablesCategory() {
        var vegetables = new ProductCategory("vegetables", "vgt", "Vegetables");
        return template.insert(vegetables);
    }

    @Test
    @Transactional
    public void testSaveProductCategory() {
        var vegetables = insertVegetablesCategory();
        assertThat(vegetables).isNotNull();
    }

    @Test
    @Transactional
    public void testSaveProduct() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        template.insert(carrot);
        var reloaded = products.findById(carrot.sku);
        assertThat(reloaded).isNotEmpty();
        assertThat(reloaded.get().category.getId()).isEqualTo(vegetables.label);
    }

    @Test
    @Transactional
    public void testSaveInventory() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var inventory200gm = new Inventory("200gm", "15", Instant.now(), 100);
        var inventory500gm = new Inventory("500gm", "25", Instant.now(), 100);
        carrot.addAll(inventory200gm, inventory500gm);
        template.insert(carrot);
        var reloaded = inventories.findByIdAndSku(inventory200gm.id, carrot.sku);
        assertThat(reloaded).isNotEmpty();
        assertThat(reloaded.get()).isEqualTo(inventory200gm);
        var inventoriesFromRepo = inventories.findAllBySku(carrot.sku);
        assertThat(inventoriesFromRepo).containsAll(carrot.inventories);
    }
}