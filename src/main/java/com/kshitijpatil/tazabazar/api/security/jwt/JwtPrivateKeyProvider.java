package com.kshitijpatil.tazabazar.api.security.jwt;

import com.kshitijpatil.tazabazar.utils.Base64Util;
import com.kshitijpatil.tazabazar.utils.ReadKeyMixin;
import com.kshitijpatil.tazabazar.utils.ResourceUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
@RequiredArgsConstructor
public class JwtPrivateKeyProvider implements ReadKeyMixin {
    @Getter
    private final ResourceUtil resourceUtil;
    private final Base64Util base64Util;

    @Getter
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        privateKey = readKey(
                "classpath:keys/tzb_key.pkcs8.private",
                "PRIVATE",
                this::privateKeySpec,
                this::privateKeyGenerator
        );
    }

    private EncodedKeySpec privateKeySpec(String data) {
        return new PKCS8EncodedKeySpec(base64Util.decode(data));
    }

    private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            throw new JwtInitializationException(e);
        }
    }
}
