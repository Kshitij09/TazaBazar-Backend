package com.kshitijpatil.tazabazar.api.security.service;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import com.kshitijpatil.tazabazar.security.jwt.RefreshTokenNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ValidationException;

@Deprecated
public interface IUserService {
    UserView create(CreateUserRequest request) throws ValidationException;

    void storeRefreshToken(String username, String refreshToken) throws UsernameNotFoundException;

    UserView findUserWithRefreshToken(String refreshToken) throws RefreshTokenNotFoundException;
}
