package com.kshitijpatil.tazabazar.apiv2.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserView {
    public String username;
    public String fullName;
    public String phone;
    public boolean emailVerified;
    public boolean phoneVerified;
}
