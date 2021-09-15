package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.api.security.model.Role;
import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table("user_detail")
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
@Data
@NoArgsConstructor
public class User implements Serializable, UserDetails {
    @Id
    public String username;
    public String password;
    public String fullName;
    public String phone;
    public String refreshToken;
    public boolean emailVerified;
    public boolean phoneVerified;
    @MappedCollection(idColumn = "username")
    public Set<CartItem> cart = new HashSet<>();

    @Transient
    private final boolean isAccountNonExpired = true;
    @Transient
    private final boolean isAccountNonLocked = true;
    @Transient
    private final boolean isCredentialsNonExpired = true;
    @Transient
    private final boolean isEnabled = true;
    @Transient
    private final Set<Role> authorities = new HashSet<>();

    public User(String username, String password, String fullName, String phone, String refreshToken) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.refreshToken = refreshToken;
    }

    public User(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public void addToCart(Inventory inventory, Long quantity) {
        cart.add(new CartItem(inventory, quantity));
    }

    public void clearCart() {
        cart.clear();
    }
}
