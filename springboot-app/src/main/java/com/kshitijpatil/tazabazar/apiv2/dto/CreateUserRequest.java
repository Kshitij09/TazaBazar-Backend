package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
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
    public Set<String> authorities = new HashSet<>();


    public CreateUserRequest(@NonNull String username, @NonNull String password, @NonNull String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.authorities = new HashSet<>();
        this.fullName = null;
    }

    public void addAuthority(String authority) {
        authorities.add(authority);
    }
}