package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAuthView {
    public String username;
    public String fullName;
    public String phone;
    public boolean emailVerified;
    public boolean phoneVerified;
}
