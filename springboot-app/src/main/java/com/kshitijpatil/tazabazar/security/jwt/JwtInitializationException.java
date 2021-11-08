package com.kshitijpatil.tazabazar.security.jwt;

public class JwtInitializationException extends RuntimeException {
    public JwtInitializationException(Throwable e) {
        super("Error initializing public/private key!", e);
    }
}