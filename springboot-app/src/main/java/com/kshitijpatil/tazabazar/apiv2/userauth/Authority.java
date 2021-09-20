package com.kshitijpatil.tazabazar.apiv2.userauth;

import lombok.Value;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Value
@Table("grants")
public class Authority implements GrantedAuthority {
    public AggregateReference<Role, String> role;

    @Override
    public String getAuthority() {
        return role.getId();
    }
}