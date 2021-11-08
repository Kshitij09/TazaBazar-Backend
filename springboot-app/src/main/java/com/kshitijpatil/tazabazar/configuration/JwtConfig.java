package com.kshitijpatil.tazabazar.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private Long expirationInMinutes;
    private String privateKeyFilepath;
    private String publicKeyFilepath;
}