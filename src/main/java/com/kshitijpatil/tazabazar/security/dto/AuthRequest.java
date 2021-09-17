package com.kshitijpatil.tazabazar.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AuthRequest {
    @NotNull
    @Email
    private String username;
    @NotNull
    private String password;
}
