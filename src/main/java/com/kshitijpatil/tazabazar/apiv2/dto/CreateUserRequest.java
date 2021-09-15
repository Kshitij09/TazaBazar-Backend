package com.kshitijpatil.tazabazar.apiv2.dto;

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
    @NotBlank
    @NonNull
    public String phone;
    public Set<String> authorities = Collections.emptySet();
}