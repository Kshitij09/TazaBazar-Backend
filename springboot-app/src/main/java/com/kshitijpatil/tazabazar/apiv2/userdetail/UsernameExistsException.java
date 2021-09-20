package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameExistsException extends RuntimeException implements ApiError {
    @Getter
    private final String error = "userdetail-001";
    private final String username;

    @Override
    public String getMessage() {
        return String.format("Username: %s already exists!", username);
    }
}
