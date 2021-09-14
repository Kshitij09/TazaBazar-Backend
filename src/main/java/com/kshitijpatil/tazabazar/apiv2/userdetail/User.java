package com.kshitijpatil.tazabazar.apiv2.userdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_detail")
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
@Data
public class User {
    @Id
    public final Long id;
    public String username;
    public String password;
    public String fullName;
    public String phone;
    public String refreshToken;
    public boolean emailVerified;
    public boolean phoneVerified;

    public User(String username, String password, String fullName, String phone, String refreshToken) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.refreshToken = refreshToken;
    }
}
