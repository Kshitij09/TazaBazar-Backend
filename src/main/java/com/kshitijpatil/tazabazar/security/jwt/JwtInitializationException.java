package com.kshitijpatil.tazabazar.security.jwt;

public class JwtInitializationException extends RuntimeException {
    public JwtInitializationException(Throwable e) {
        super("Something went wong while reading private key!", e);
    }
}