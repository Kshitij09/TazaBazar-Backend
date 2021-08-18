package com.kshitijpatil.tazabazar.api.security.service;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import com.kshitijpatil.tazabazar.api.security.jwt.RefreshTokenNotFoundException;
import com.kshitijpatil.tazabazar.api.security.mappers.CreateUserRequestMapper;
import com.kshitijpatil.tazabazar.api.security.mappers.UserMapper;
import com.kshitijpatil.tazabazar.api.security.model.User;
import com.kshitijpatil.tazabazar.api.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import static java.lang.String.format;

@Service
public class UserService implements IUserService {

    @Autowired
    @Qualifier("in_memory_user_repository")
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username - %s, not found", username)));
    }

    @Override
    public UserView create(CreateUserRequest request) throws ValidationException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }
        User user = CreateUserRequestMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);
        return UserMapper.toUserView(user);
    }

    @Override
    public void storeRefreshToken(String username, String refreshToken) throws UsernameNotFoundException {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        user.setRefreshToken(refreshToken);
        userRepository.update(user);
    }

    @Override
    public UserView findUserWithRefreshToken(String refreshToken) throws RefreshTokenNotFoundException {
        var user = userRepository.findByRefreshToken(refreshToken);
        if (user.isPresent()) {
            return UserMapper.toUserView(user.get());
        } else {
            throw new RefreshTokenNotFoundException();
        }
    }
}
