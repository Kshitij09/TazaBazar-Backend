package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.api.ApiError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhoneExistsException extends RuntimeException implements ApiError {
    @Getter
    private final String error = "userdetail-002";
    private final String phone;

    @Override
    public String getMessage() {
        return String.format("User with Phone: %s already exists!", phone);
    }
}
