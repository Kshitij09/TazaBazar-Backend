package com.kshitijpatil.tazabazar.apiv2.userauth;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;

@Value
@Table("user_role")
public class Role {
    public static String ROLE_USER = "ROLE_USER";
    public static String ROLE_ADMIN = "ROLE_ADMIN";
    public static String ROLE_VENDOR = "ROLE_VENDOR";
    @NotBlank
    @Id
    public String name;
}
