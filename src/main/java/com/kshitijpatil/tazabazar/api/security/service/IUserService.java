package com.kshitijpatil.tazabazar.api.security.service;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import com.kshitijpatil.tazabazar.api.security.jwt.RefreshTokenNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ValidationException;

public interface IUserService extends UserDetailsService {
    UserView create(CreateUserRequest request) throws ValidationException;

    void storeRefreshToken(String username, String refreshToken) throws UsernameNotFoundException;

    UserView findUserWithRefreshToken(String refreshToken) throws RefreshTokenNotFoundException;
}
