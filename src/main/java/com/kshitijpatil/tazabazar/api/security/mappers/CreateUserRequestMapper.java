package com.kshitijpatil.tazabazar.api.security.mappers;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.model.Role;
import com.kshitijpatil.tazabazar.api.security.model.User;

import java.util.stream.Collectors;

public class CreateUserRequestMapper {
    public static User toUser(CreateUserRequest createUserRequest) {
        var user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(createUserRequest.getPassword());
        user.setFullName(createUserRequest.getFullName());
        var authorities = createUserRequest.getAuthorities()
                .stream()
                .map(Role::new)
                .collect(Collectors.toSet());
        user.setAuthorities(authorities);
        return user;
    }
}
