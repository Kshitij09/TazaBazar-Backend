package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {
    private UserView user;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private Set<String> authorities;
}
