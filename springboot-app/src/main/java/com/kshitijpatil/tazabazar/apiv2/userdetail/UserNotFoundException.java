package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNotFoundException extends RuntimeException implements ApiError {
    @Getter
    public final String error = "user-001";
    private final String username;

    @Override
    public String getMessage() {
        return String.format("User with username='%s' not found", username);
    }
}