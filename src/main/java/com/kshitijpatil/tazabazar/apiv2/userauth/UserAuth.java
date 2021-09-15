package com.kshitijpatil.tazabazar.apiv2.userauth;

import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("user_auth")
@Data
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
public class UserAuth {
    @Id
    public final AggregateReference<User, String> username;
    public String password;
    public String refreshToken;
    public boolean emailVerified = false;
    public boolean phoneVerified = false;
    @MappedCollection(idColumn = "username")
    public Set<Authority> grantedAuthorities = new HashSet<>();

    public UserAuth(AggregateReference<User, String> username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean add(Authority authority) {
        return grantedAuthorities.add(authority);
    }
}
