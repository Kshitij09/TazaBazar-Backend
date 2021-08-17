package com.kshitijpatil.tazabazar.api.security.repository;

import com.kshitijpatil.tazabazar.api.security.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    User save(User user);
}
