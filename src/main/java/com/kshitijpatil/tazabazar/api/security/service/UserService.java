package com.kshitijpatil.tazabazar.api.security.service;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.ValidationException;

public interface UserService extends UserDetailsService {
    UserView create(CreateUserRequest request) throws ValidationException;
}
