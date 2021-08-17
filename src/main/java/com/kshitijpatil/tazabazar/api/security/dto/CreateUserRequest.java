package com.kshitijpatil.tazabazar.api.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshitijpatil.tazabazar.api.security.model.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class CreateUserRequest {
    @NotBlank
    @Email
    private String username;
    @NotBlank
    @JsonProperty("full_name")
    private String fullName;
    @NotBlank
    private String password;
    private Set<String> authorities = Set.of(Role.USER);
}
