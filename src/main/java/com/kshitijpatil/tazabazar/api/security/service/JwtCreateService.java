package com.kshitijpatil.tazabazar.api.security.service;

import com.kshitijpatil.tazabazar.api.security.jwt.JwtPrivateKeyProvider;
import com.kshitijpatil.tazabazar.api.utils.DateUtil;
import com.kshitijpatil.tazabazar.configuration.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtCreateService {
    private static final String CLAIM_USERNAME = "username";
    private final JwtConfig jwtConfig;
    private final JwtPrivateKeyProvider jwtPrivateKeyProvider;
    private final DateUtil dateUtil;
    private final SecureRandom secureRandom = new SecureRandom();
    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final String jwtIssuer = "com.kshitijpatil.tazabazar";


    public String generateToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusMinutes(jwtConfig.getExpirationInMinutes());
        return Jwts.builder()
                .setIssuedAt(dateUtil.toDate(now))
                .setExpiration(dateUtil.toDate(expiryDate))
                .setIssuer(jwtIssuer)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.RS256, jwtPrivateKeyProvider.getPrivateKey())
                .claim(CLAIM_USERNAME, username)
                .compact();
    }

    public String generateRefreshToken() {
        var salt = secureRandom.nextInt();
        var content = String.format("%d|%s|%d", salt, jwtSecret, System.currentTimeMillis());
        try {
            var md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(content.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Something went wrong while generating refreshToken");
        }
    }

}
