package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.transaction.annotation.Transactional;

import static com.kshitijpatil.tazabazar.apiv2.TestUtils.assertNotEmptyAndGet;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseRepositoryTest {
    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    UserRepository users;

    private User saveUser1() {
        var user = new User("johndoe@test.com",
                "1234",
                "John Doe",
                "+919090909090",
                "sajgf218y9ofba");
        return users.save(user);
    }

    private User saveUser2() {
        var user2 = new User("jerrycan@test.com",
                "7856",
                "Jerry Can",
                "+918219401924",
                "v7t912ofvbas");
        return users.save(user2);
    }

    @Test
    @Transactional
    public void testRegisterUser() {
        var user1 = saveUser1();
        saveUser2();
        assertThat(user1.id).isNotNull();
        var reloaded = assertNotEmptyAndGet(users.findById(user1.id));
        assertThat(reloaded).isEqualTo(user1);
    }

    @Test
    @Transactional
    public void testFindByUsernamePassword() {
        var user = saveUser1();
        var reloadedUser = assertNotEmptyAndGet(users.findByUsernameAndPassword(user.username, user.password));
        assertThat(reloadedUser.id).isNotNull();
        assertThat(reloadedUser).isEqualTo(user);
    }

    @Test
    @Transactional
    public void testFindByUsernameRefreshToken() {
        var user = saveUser1();
        var reloadedUser = assertNotEmptyAndGet(users.findByUsernameAndRefreshToken(user.username, user.refreshToken));
        assertThat(reloadedUser.id).isNotNull();
        assertThat(reloadedUser).isEqualTo(user);
    }
}
