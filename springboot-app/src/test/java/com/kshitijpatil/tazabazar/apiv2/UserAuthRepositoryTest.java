package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.TestContext;
import com.kshitijpatil.tazabazar.apiv2.userauth.*;
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

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestContext.class)
@EnableJdbcRepositories
@ActiveProfiles("test")
public class UserAuthRepositoryTest {
    private final User user1 = new User("johndoe@test.com",
            "John Doe",
            "+919090909090");
    @Autowired
    JdbcAggregateTemplate template;
    @Autowired
    UserRepository users;
    @Autowired
    UserAuthRepository userAccounts;
    @Autowired
    RoleRepository roles;

    @BeforeEach
    public void setup() {
        roles.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateRole() {
        var userRole = new Role(Role.ROLE_USER);
        template.insert(userRole);
    }

    @Test
    @Transactional
    public void testCreateUserAccount() {
        var userDetails = template.insert(user1);
        Role userRole = new Role(Role.ROLE_USER), adminRole = new Role(Role.ROLE_ADMIN);
        var userAuthority = new Authority(AggregateReference.to(template.insert(userRole).name));
        var adminAuthority = new Authority(AggregateReference.to(template.insert(adminRole).name));
        var userAuth = new UserAuth(AggregateReference.to(userDetails.username), "1234");
        userAuth.grantedAuthorities.add(userAuthority);
        userAuth.grantedAuthorities.add(adminAuthority);
        template.insert(userAuth);
        var grantedAuthorities = userAuth.grantedAuthorities.stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());
        var reloadedAuthorities = StreamSupport.stream(roles.findAllById(grantedAuthorities).spliterator(), false)
                .map(Role::getName)
                .collect(Collectors.toList());
        assertThat(reloadedAuthorities).containsAll(grantedAuthorities);
    }

    @Test
    @Transactional
    public void testFindByRefreshToken() {
        var userDetails = template.insert(user1);
        var userAuth = new UserAuth(AggregateReference.to(userDetails.username), "1234");
        Role userRole = new Role(Role.ROLE_USER), adminRole = new Role(Role.ROLE_ADMIN);
        var userAuthority = new Authority(AggregateReference.to(template.insert(userRole).name));
        var adminAuthority = new Authority(AggregateReference.to(template.insert(adminRole).name));
        userAuth.grantedAuthorities.add(userAuthority);
        userAuth.grantedAuthorities.add(adminAuthority);
        template.insert(userAuth);
        userAuth.refreshToken = "asugksabgs";
        userAccounts.save(userAuth);
        var reloaded = userAccounts.findByRefreshToken(userAuth.refreshToken);
        assertThat(reloaded).isNotEmpty();
        assertThat(reloaded.get()).isEqualTo(userAuth);
    }
}
