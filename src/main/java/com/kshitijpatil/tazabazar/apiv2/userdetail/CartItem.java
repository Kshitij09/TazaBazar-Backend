package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Value
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
public class CartItem {
    @Positive
    @Max(8)
    @EqualsAndHashCode.Exclude
    public Long quantity;
    Long inventoryId;

    public CartItem(Inventory inventory, Long quantity) {
        this.inventoryId = inventory.id;
        this.quantity = quantity;
    }
}
