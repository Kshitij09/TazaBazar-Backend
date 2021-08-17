package com.kshitijpatil.tazabazar.api.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {
    private final boolean isAccountNonExpired = true;
    private final boolean isAccountNonLocked = true;
    private final boolean isCredentialsNonExpired = true;
    private final boolean isEnabled = true;
    private int id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String fullName;
    private Set<Role> authorities = new HashSet<>();
}
