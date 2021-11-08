package com.kshitijpatil.tazabazar.security.jwt;

import com.kshitijpatil.tazabazar.configuration.JwtConfig;
import com.kshitijpatil.tazabazar.utils.ResourceUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Component
@RequiredArgsConstructor
public class JwtPublicKeyProvider {
    @Getter
    private final ResourceUtil resourceUtil;
    private final JwtConfig jwtConfig;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            var keyInputStream = resourceUtil.getInputStream(jwtConfig.getPublicKeyFilepath());
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(keyInputStream);
            publicKey = cert.getPublicKey();
        } catch (CertificateException | IOException ex) {
            throw new JwtInitializationException(ex);
        }
    }

    public PublicKey get() {
        return publicKey;
    }
}
