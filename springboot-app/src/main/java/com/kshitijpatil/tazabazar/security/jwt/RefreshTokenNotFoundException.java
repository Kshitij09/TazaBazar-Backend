package com.kshitijpatil.tazabazar.security.jwt;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;

public class RefreshTokenNotFoundException extends RuntimeException implements ApiError {
    @Getter
    private final String error = "auth-001";

    @Override
    public String getMessage() {
        return "Invalid refresh token";
    }
}
