package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table("user_detail")
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
@Data
public class User implements Serializable {
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

    public User(String username, String password, String fullName, String phone, String refreshToken) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.refreshToken = refreshToken;
    }

    public void addToCart(Inventory inventory, Long quantity) {
        cart.add(new CartItem(inventory, quantity));
    }

    public void clearCart() {
        cart.clear();
    }
}
