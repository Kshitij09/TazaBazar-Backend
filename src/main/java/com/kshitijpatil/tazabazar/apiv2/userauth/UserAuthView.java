package com.kshitijpatil.tazabazar.apiv2.userauth;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserAuthView {
    public String username;
    public String fullName;
    public String phone;
    public boolean emailVerified;
    public boolean phoneVerified;
}
