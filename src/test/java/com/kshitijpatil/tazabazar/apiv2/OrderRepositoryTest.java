package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.apiv2.order.Order;
import com.kshitijpatil.tazabazar.apiv2.order.OrderRepository;
import com.kshitijpatil.tazabazar.apiv2.product.*;
import com.kshitijpatil.tazabazar.util.TestPostgreConfig;
import org.junit.jupiter.api.BeforeEach;
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
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orders;

    @Autowired
    InventoryRepository inventories;

    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    private ProductRepository products;
    @Autowired
    private ProductCategoryRepository productCategories;

    private ProductCategory insertVegetablesCategory() {
        var vegetables = new ProductCategory("vegetables", "vgt", "Vegetables");
        return template.insert(vegetables);
    }

    @BeforeEach
    public void setup() {
        var vegetables = insertVegetablesCategory();
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var inventory200gm = new Inventory("200gm", "15", Instant.now(), 100);
        var inventory500gm = new Inventory("500gm", "25", Instant.now(), 100);
        carrot.addAll(inventory200gm, inventory500gm);
        template.insert(carrot);
    }

    @Test
    @Transactional
    public void testCreateOrder() {
        var inv1 = inventories.findByIdAndSku(1L, "vgt-001");
        var inv2 = inventories.findByIdAndSku(2L, "vgt-001");
        assertThat(inv1).isNotEmpty();
        assertThat(inv2).isNotEmpty();
        var order = new Order(Instant.now(), "Accepted");
        order.addOrderLine(inv1.get(), 4L);
        order.addOrderLine(inv2.get(), 6L);
        var saved = orders.save(order);
        var reloaded = orders.findById(saved.getId());
        assertThat(reloaded).isNotEmpty();
        assertThat(reloaded.get().getOrderLines()).containsExactly(
                Order.createOrderLine(inv1.get(), 4L),
                Order.createOrderLine(inv2.get(), 6L)
        );
    }
}
