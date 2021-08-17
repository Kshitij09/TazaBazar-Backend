package com.kshitijpatil.tazabazar.api.security.repository;

import com.kshitijpatil.tazabazar.api.security.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository("in_memory_user_repository")
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersMap = new ConcurrentSkipListMap<>();
    private final AtomicInteger globalUserId = new AtomicInteger(1);

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersMap.get(username));
    }

    @Override
    public User save(User user) {
        user.setId(globalUserId.getAndIncrement());
        usersMap.put(user.getUsername(), user);
        return user;
    }
}
