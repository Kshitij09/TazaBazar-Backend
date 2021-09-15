package com.kshitijpatil.tazabazar.api.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Deprecated
public class UserView {
    private String username;
    @JsonProperty("full_name")
    private String fullName;
    private List<String> roles;
}
