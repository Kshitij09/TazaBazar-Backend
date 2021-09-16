package com.kshitijpatil.tazabazar.apiv2.userauth;

import com.kshitijpatil.tazabazar.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleNotFoundException extends RuntimeException implements ApiError {
    @Getter
    private final String error = "role-001";
    private final String role;

    @Override
    public String getMessage() {
        return String.format("Role: %s, not found", role);
    }
}
