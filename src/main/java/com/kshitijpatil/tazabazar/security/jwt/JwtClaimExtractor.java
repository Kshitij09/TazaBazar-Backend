package com.kshitijpatil.tazabazar.security.jwt;

import io.jsonwebtoken.Claims;

import java.util.List;

public class JwtClaimExtractor {
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_ROLES = "roles";
    private final Claims claims;

    public JwtClaimExtractor(Claims claims) {
        this.claims = claims;
    }

    public String getUsername() {
        return (String) claims.get(CLAIM_USERNAME);
    }

    public List<String> getRoles() {
        var roles = (String[]) claims.get(CLAIM_ROLES);
        return List.of(roles);
    }
}
