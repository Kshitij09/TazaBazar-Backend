package com.kshitijpatil.tazabazar.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponse {
    @JsonProperty("access_token")
    private final String accessToken;
}
