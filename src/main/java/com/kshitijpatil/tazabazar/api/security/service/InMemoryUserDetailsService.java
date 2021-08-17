package com.kshitijpatil.tazabazar.api.security.service;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import com.kshitijpatil.tazabazar.api.security.mappers.CreateUserRequestMapper;
import com.kshitijpatil.tazabazar.api.security.mappers.UserMapper;
import com.kshitijpatil.tazabazar.api.security.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Service("in_memory_user_details")
public class InMemoryUserDetailsService implements UserService {
    private final Map<String, UserDetails> usersMap = new HashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public InMemoryUserDetailsService() {
        var user = new User("kshitij", "1234", "Kshitij Patil");
        usersMap.put(user.getUsername(), user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userDetails = usersMap.get(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(format("User with username='%s' not found", username));
        } else {
            return userDetails;
        }
    }

    @Override
    public UserView create(CreateUserRequest request) throws ValidationException {
        if (usersMap.containsKey(request.getUsername())) {
            throw new ValidationException("Username exists!");
        }
        User user = CreateUserRequestMapper.toUser(request);
        user.setId(secureRandom.nextInt());
        usersMap.put(user.getUsername(), user);
        return UserMapper.toUserView(user);
    }
}
