package com.kshitijpatil.tazabazar.api.security.repository;

import com.kshitijpatil.tazabazar.api.security.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository("in_memory_user_repository")
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usernameToUserMap = new ConcurrentHashMap<>();
    private final Map<String, User> refreshTokenToUserMap = new ConcurrentHashMap<>();
    private final AtomicInteger globalUserId = new AtomicInteger(1);

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usernameToUserMap.get(username));
    }

    @Override
    public Optional<User> findByRefreshToken(String refreshToken) {
        return Optional.ofNullable(refreshTokenToUserMap.get(refreshToken));
    }

    @Override
    public User save(User user) {
        user.setId(globalUserId.getAndIncrement());
        usernameToUserMap.put(user.getUsername(), user);
        if (Optional.ofNullable(user.getRefreshToken()).isPresent()) {
            // Usually refreshTokens are assigned later, so we won't
            // put it in refreshTokenMap until later.
            refreshTokenToUserMap.put(user.getRefreshToken(), user);
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        var oldEntry = usernameToUserMap.put(user.getUsername(), user);
        refreshTokenToUserMap.put(user.getRefreshToken(), user);
        return oldEntry != null;
    }
}
