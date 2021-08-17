package com.kshitijpatil.tazabazar.api.security;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.model.Role;
import com.kshitijpatil.tazabazar.api.security.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class InMemoryUserInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    UserService userService;
    @Autowired
    Logger logger;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        var userRequest = new CreateUserRequest("kshitij", "1234", "Kshitij Patil");
        userRequest.setAuthorities(Set.of(Role.USER));
        userService.create(userRequest);

        var adminRequest = new CreateUserRequest("admin", "0000", "John Doe");
        adminRequest.setAuthorities(Set.of(Role.ADMIN));
        logger.info("Loaded 2 default users");
        userService.create(adminRequest);
    }
}
