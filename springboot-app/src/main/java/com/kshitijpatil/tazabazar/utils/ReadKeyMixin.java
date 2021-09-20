package com.kshitijpatil.tazabazar.utils;

import com.kshitijpatil.tazabazar.security.jwt.JwtInitializationException;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ReadKeyMixin {
    ResourceUtil getResourceUtil();

    default <T extends Key> T readKey(String resourcePath, String headerSpec, Function<String, EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
        try {
            String keyString = getResourceUtil().asString(resourcePath);
            //TODO you can check the headers and throw an exception here if you want

            keyString = keyString
                    .replace("-----BEGIN " + headerSpec + " KEY-----", "")
                    .replace("-----END " + headerSpec + " KEY-----", "")
                    .replaceAll("\\s+", "");

            return keyGenerator.apply(KeyFactory.getInstance("RSA"), keySpec.apply(keyString));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new JwtInitializationException(e);
        }
    }
}
