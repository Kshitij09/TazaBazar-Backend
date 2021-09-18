package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.TestContext;
import com.kshitijpatil.tazabazar.apiv2.order.Order;
import com.kshitijpatil.tazabazar.apiv2.order.OrderRepository;
import com.kshitijpatil.tazabazar.apiv2.order.OrderStatus;
import com.kshitijpatil.tazabazar.apiv2.product.*;
import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.kshitijpatil.tazabazar.apiv2.TestUtils.assertNotEmptyAndGet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestContext.class)
@EnableJdbcRepositories
@ActiveProfiles("test")
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orders;

    @Autowired
    InventoryRepository inventories;

    @Autowired
    UserRepository users;

    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    private ProductRepository products;
    @Autowired
    private ProductCategoryRepository productCategories;
    private final Random random = new Random();

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
        var inventory200gm = new Inventory("200gm", 15., Instant.now(), 100);
        var inventory500gm = new Inventory("500gm", 25., Instant.now(), 100);
        carrot.addAll(inventory200gm, inventory500gm);
        template.insert(carrot);
    }


    @Test
    @Transactional
    public void testCreateOrder() {
        User user1 = new User("johndoe@test.com",
                "John Doe",
                "+919090909090");
        var savedUser = template.insert(user1);

        var vegetableInventories = inventories.findAllBySku("vgt-001");
        var order = new Order(AggregateReference.to(savedUser.username), Instant.now(), OrderStatus.ACCEPTED);
        var orderLines = StreamSupport.stream(vegetableInventories.spliterator(), false)
                .map(inv -> Order.createOrderLine(inv, random.nextInt(7) + 1L))
                .collect(Collectors.toList());
        order.addAll(orderLines);
        var saved = orders.save(order);
        var reloaded = assertNotEmptyAndGet(orders.findById(saved.getId()));
        assertThat(reloaded.getOrderLines()).containsAll(orderLines);
    }

    @Test
    @Transactional
    public void testGetUsernameByOrderId() {
        User user1 = new User("johndoe@test.com",
                "John Doe",
                "+919090909090");
        var savedUser = template.insert(user1);

        var vegetableInventories = inventories.findAllBySku("vgt-001");
        var order = new Order(AggregateReference.to(savedUser.username), Instant.now(), OrderStatus.ACCEPTED);
        var orderLines = StreamSupport.stream(vegetableInventories.spliterator(), false)
                .map(inv -> Order.createOrderLine(inv, random.nextInt(7) + 1L))
                .collect(Collectors.toList());
        order.addAll(orderLines);
        var saved = orders.save(order);
        assertThat(saved.id).isNotNull();
        var actual = orders.getUsernameById(saved.id);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(user1.username);
    }
}
