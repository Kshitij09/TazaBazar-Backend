package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.TestContext;
import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import com.kshitijpatil.tazabazar.apiv2.product.InventoryRepository;
import com.kshitijpatil.tazabazar.apiv2.product.Product;
import com.kshitijpatil.tazabazar.apiv2.product.ProductCategory;
import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UserRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import static com.kshitijpatil.tazabazar.apiv2.TestUtils.assertNotEmptyAndGet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestContext.class)
@EnableJdbcRepositories
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    UserRepository users;
    @Autowired
    InventoryRepository inventories;
    private final Random random = new Random();

    private final User user1 = new User("johndoe@test.com",
            "John Doe",
            "+919090909090");

    private final User user2 = new User("jerrycan@test.com",
            "Jerry Can",
            "+918219401924");

    @Test
    @Transactional
    public void testRegisterUser() {
        var user = template.insert(user1);
        var reloaded = assertNotEmptyAndGet(users.findById(user.username));
        assertThat(reloaded).isEqualTo(user);
    }

    private List<Inventory> populateProductInventories() {
        var vegetables = new ProductCategory("vegetables", "vgt", "Vegetables");
        template.insert(vegetables);
        var carrot = new Product(String.format("%s-001", vegetables.skuPrefix),
                "Carrot",
                AggregateReference.to(vegetables.label));
        var inventory200gm = new Inventory("200gm", 15., Instant.now(), 100);
        var inventory500gm = new Inventory("500gm", 25., Instant.now(), 100);
        carrot.addAll(inventory200gm, inventory500gm);
        template.insert(carrot);
        return List.of(inventory200gm, inventory500gm);
    }

    @Test
    @Transactional
    public void testUniquePhoneConstraint() {
        template.insert(user1);
        var newUser = SerializationUtils.clone(user1);
        newUser.username = "mike@test.com";
        try {
            template.insert(newUser);
        } catch (DbActionExecutionException exception) {
            var cause = exception.getCause();
            assertThat(cause).isInstanceOf(DuplicateKeyException.class);
            assertThat(cause.getMessage()).contains("violates unique constraint \"user_detail_phone_key\"");
        }
    }

    @Test
    @Transactional
    public void testUniqueUsernameConstraint() {
        template.insert(user1);
        var newUser = SerializationUtils.clone(user1);
        newUser.phone = "1234567890";
        try {
            template.insert(newUser);
        } catch (DbActionExecutionException exception) {
            var cause = exception.getCause();
            assertThat(cause).isInstanceOf(DuplicateKeyException.class);
            assertThat(cause.getMessage()).contains("violates unique constraint \"user_detail_pkey\"");
        }
    }

    @Test
    @Transactional
    public void testUserCart() {
        User user = SerializationUtils.clone(user2);
        var savedInventories = populateProductInventories();
        savedInventories.forEach(inv -> user.addToCart(inv, random.nextInt(7) + 1L));
        template.insert(user);
    }
}
