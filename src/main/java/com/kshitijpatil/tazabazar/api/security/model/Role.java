package com.kshitijpatil.tazabazar.api.security.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@RequiredArgsConstructor
public class Role implements GrantedAuthority {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    @NonNull
    private String authority;

}