package com.kshitijpatil.tazabazar.api.security.jwt;

import com.kshitijpatil.tazabazar.utils.Base64Util;
import com.kshitijpatil.tazabazar.utils.ReadKeyMixin;
import com.kshitijpatil.tazabazar.utils.ResourceUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Component
@RequiredArgsConstructor
public class JwtPublicKeyProvider implements ReadKeyMixin {
    @Getter
    private final ResourceUtil resourceUtil;
    private final Base64Util base64Util;

    @Getter
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        publicKey = readKey(
                "classpath:keys/tzb_key.x509.public",
                "PUBLIC",
                this::publicKeySpec,
                this::publicKeyGenerator
        );
    }

    private EncodedKeySpec publicKeySpec(String data) {
        return new X509EncodedKeySpec(base64Util.decode(data));
    }

    private PublicKey publicKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            throw new JwtInitializationException(e);
        }
    }
}
