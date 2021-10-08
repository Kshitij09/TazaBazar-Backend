package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.TestContext;
import com.kshitijpatil.tazabazar.apiv2.product.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestContext.class)
@EnableJdbcRepositories
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
    public void testFindProductsByCategory() {
        var vegetables = insertVegetablesCategory();
        var fruits = template.insert(
                new ProductCategory("fruits", "fru", "Fruits")
        );
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var sitafal = new Product(String.format("%s-001", fruits.skuPrefix),
                "Sitagal",
                AggregateReference.to(fruits.label));
        template.insert(carrot);
        template.insert(sitafal);
        var reloaded = products.findByCategory(vegetables.label);
        assertThat(reloaded).containsOnly(carrot);
        reloaded = products.findByCategory(fruits.label);
        assertThat(reloaded).containsOnly(sitafal);
    }

    @Test
    @Transactional
    public void testSaveInventory() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var inventory200gm = new Inventory("200gm", 15.0, Instant.now(), 100);
        var inventory500gm = new Inventory("500gm", 25.0, Instant.now(), 100);
        carrot.addAll(inventory200gm, inventory500gm);
        template.insert(carrot);
        var reloaded = inventories.findById(inventory200gm.id);
        assertThat(reloaded).isNotEmpty();
        assertThat(reloaded.get()).isEqualTo(inventory200gm);
        var inventoriesFromRepo = inventories.findAllBySku(carrot.sku);
        assertThat(inventoriesFromRepo).containsAll(carrot.inventories);
    }

    @Test
    @Transactional
    public void testInventoryFindAllByIdAndSkus() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var inventory200gm = new Inventory("200gm", 15.0, Instant.now(), 100);
        var inventory500gm = new Inventory("500gm", 25.0, Instant.now(), 100);
        carrot.addAll(inventory200gm, inventory500gm);
        template.insert(carrot);
        var reloaded = inventories.findAllById(
                Arrays.asList(inventory200gm.id, inventory500gm.id)
        );
        assertThat(reloaded).isNotEmpty();
    }

    @Test
    @Transactional
    public void inventoryProductSkuAndQuantityLabelShouldBeUnique() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var inventory200gm = new Inventory("200gm", 15., Instant.now(), 100);
        var inventory200gm2 = new Inventory("200gm", 20., Instant.now(), 20);
        carrot.addAll(inventory200gm, inventory200gm2);
        assertThatThrownBy(() -> template.insert(carrot)).hasCauseInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @Transactional
    public void testSkuExists() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        template.insert(carrot);
        assertThat(products.skuExists(carrot.sku)).isTrue();
    }

    @Test
    @Transactional
    public void testSearchProductByName() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var leafyVegetables = template.insert(new ProductCategory("leafy-vegetables",
                "lfvgt",
                "Leafy Vegetables"));
        var kadiPatta = new Product(String.format("%s-001", leafyVegetables.skuPrefix),
                "Kadi Patta / Curry Leaves",
                AggregateReference.to(leafyVegetables.label));
        var tomato = new Product(String.format("%s-002", vegetables.skuPrefix),
                "Tomato Red",
                AggregateReference.to(vegetables.label));
        template.insert(kadiPatta);
        template.insert(tomato);
        template.insert(carrot);
        assertThat(products.searchProductByName("sagkkjal")).isEmpty();
        assertThat(products.searchProductByName("cur:*")).containsOnly(kadiPatta);
        assertThat(products.searchProductByName("tom:*")).containsOnly(tomato);
        assertThat(products.searchProductByName("car:*")).containsOnly(carrot);
        assertThat(products.searchProductByName("carrots:*")).containsOnly(carrot);
    }

    @Test
    @Transactional
    public void testSearchProductByCategoryAndName() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var leafyVegetables = template.insert(new ProductCategory("leafy-vegetables",
                "lfvgt",
                "Leafy Vegetables"));
        var kadiPatta = new Product(String.format("%s-001", leafyVegetables.skuPrefix),
                "Kadi Patta / Curry Leaves",
                AggregateReference.to(leafyVegetables.label));
        template.insert(kadiPatta);
        template.insert(carrot);
        assertThat(products.searchProductByCategoryAndName(vegetables.label, "kadi")).isEmpty();
        assertThat(products.searchProductByCategoryAndName(vegetables.label, "carrot")).containsOnly(carrot);
        assertThat(products.searchProductByCategoryAndName(leafyVegetables.label, "carrot")).isEmpty();
        assertThat(products.searchProductByCategoryAndName(leafyVegetables.label, "curry leave")).containsOnly(kadiPatta);
    }
}