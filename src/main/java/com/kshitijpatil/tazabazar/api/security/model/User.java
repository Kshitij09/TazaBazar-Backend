package com.kshitijpatil.tazabazar.api.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class User implements UserDetails {
    private final boolean isAccountNonExpired = true;
    private final boolean isAccountNonLocked = true;
    private final boolean isCredentialsNonExpired = true;
    private final boolean isEnabled = true;
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String refreshToken;
    private Set<Role> authorities = new HashSet<>();

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public List<String> getAuthorityStrings() {
        return authorities.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
    }
}
