package com.kshitijpatil.tazabazar.security.jwt;

import com.kshitijpatil.tazabazar.configuration.JwtConfig;
import com.kshitijpatil.tazabazar.utils.ResourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
@RequiredArgsConstructor
public class JwtPrivateKeyProvider {
    private final ResourceUtil resourceUtil;
    private final JwtConfig jwtConfig;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            var keyBytes = resourceUtil.readAllBytes(jwtConfig.getPrivateKeyFilepath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new JwtInitializationException(ex);
        }
    }

    public PrivateKey get() {
        return privateKey;
    }
}
