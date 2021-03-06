package com.kshitijpatil.tazabazar.security;


import com.kshitijpatil.tazabazar.security.jwt.JwtPublicKeyProvider;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtValidateService {
    private final JwtPublicKeyProvider jwtPublicKeyProvider;
    private final Logger logger;

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtPublicKeyProvider.get())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtPublicKeyProvider.get())
                .parseClaimsJws(token)
                .getBody();
    }
}
