package com.kshitijpatil.tazabazar.api.security.model;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

@Value
public class Role implements GrantedAuthority {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String VENDOR = "VENDOR";
    @Id
    public String authority;
}