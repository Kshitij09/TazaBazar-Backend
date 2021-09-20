package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class User implements Serializable {
    @Id
    public String username;
    public String fullName;
    public String phone;
    @MappedCollection(idColumn = "username")
    public Set<CartItem> cart = new HashSet<>();

    public User(String username, String fullName, String phone) {
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
    }

    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public void addToCart(Inventory inventory, Long quantity) {
        cart.add(new CartItem(inventory, quantity));
    }

    public void clearCart() {
        cart.clear();
    }
}
