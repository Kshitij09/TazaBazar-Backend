package com.kshitijpatil.tazabazar.apiv2.initializer;

import com.kshitijpatil.tazabazar.apiv2.userauth.Role;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import com.kshitijpatil.tazabazar.utils.JsonDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final JsonDataSource jsonDataSource;
    private final IUserService userService;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Deleting all existing user accounts");
        userService.clearAll();
        log.info("Initializing roles (user,admin,vendor)");
        userService.addRole(new Role(Role.ROLE_USER));
        userService.addRole(new Role(Role.ROLE_ADMIN));
        userService.addRole(new Role(Role.ROLE_VENDOR));
        log.info("Initializing User accounts from the fixtures");
        var userAccounts = jsonDataSource.getUserAccounts();
        userAccounts.forEach(userService::createUser);
        log.info(String.format("Added %d user accounts", userAccounts.size()));
    }
}
