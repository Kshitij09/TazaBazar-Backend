package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import com.kshitijpatil.tazabazar.apiv2.product.InventoryRepository;
import com.kshitijpatil.tazabazar.apiv2.product.Product;
import com.kshitijpatil.tazabazar.apiv2.product.ProductCategory;
import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UserRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.kshitijpatil.tazabazar.apiv2.TestUtils.assertNotEmptyAndGet;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseRepositoryTest {
    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    UserRepository users;
    @Autowired
    InventoryRepository inventories;

    private final User user1 = new User("johndoe@test.com",
            "1234",
            "John Doe",
            "+919090909090",
            "sajgf218y9ofba");

    private final User user2 = new User("jerrycan@test.com",
            "7856",
            "Jerry Can",
            "+918219401924",
            "v7t912ofvbas");

    @Test
    @Transactional
    public void testRegisterUser() {
        var user = template.insert(user1);
        var reloaded = assertNotEmptyAndGet(users.findById(user.username));
        assertThat(reloaded).isEqualTo(user);
    }

    @Test
    @Transactional
    public void testFindByUsernamePassword() {
        var user = template.insert(user1);
        var reloadedUser = assertNotEmptyAndGet(users.findByUsernameAndPassword(user.username, user.password));
        assertThat(reloadedUser).isEqualTo(user);
    }

    @Test
    @Transactional
    public void testFindByUsernameRefreshToken() {
        var user = template.insert(user1);
        var reloadedUser = assertNotEmptyAndGet(users.findByRefreshToken(user.refreshToken));
        assertThat(reloadedUser).isEqualTo(user);
    }

    private void populateProductInventories() {
        var vegetables = new ProductCategory("vegetables", "vgt", "Vegetables");
        template.insert(vegetables);
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
        populateProductInventories();
        var inv1 = assertNotEmptyAndGet(inventories.findByIdAndSku(1L, "vgt-001"));
        var inv2 = assertNotEmptyAndGet(inventories.findByIdAndSku(2L, "vgt-001"));
        user.addToCart(inv1, 2L);
        user.addToCart(inv2, 3L);
        template.insert(user);
    }
}
