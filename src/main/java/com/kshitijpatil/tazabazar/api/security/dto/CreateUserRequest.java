package com.kshitijpatil.tazabazar.api.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @NonNull
    @Email
    private String username;
    @NotBlank
    @NonNull
    @JsonProperty("full_name")
    private String fullName;
    @NotBlank
    @NonNull
    private String password;
    private Set<String> authorities = Collections.emptySet();
}
