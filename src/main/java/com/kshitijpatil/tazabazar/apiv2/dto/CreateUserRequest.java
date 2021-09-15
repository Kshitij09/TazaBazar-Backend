package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Set;


@Value
public class CreateUserRequest {
    @NotBlank
    @NonNull
    @Email
    public String username;
    @NotBlank
    @NonNull
    public String password;
    @JsonProperty("full_name")
    public String fullName;
    @NotBlank
    @NonNull
    public String phone;
    public Set<String> authorities;

    public CreateUserRequest(@NonNull String username, @NonNull String password, @NonNull String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.authorities = Collections.emptySet();
        this.fullName = null;
    }
}