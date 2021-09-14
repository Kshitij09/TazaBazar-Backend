package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Value
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
public class OrderLine {
    public Long inventoryId;
    @Positive
    @Max(8)
    public Long quantity;

    public OrderLine(Inventory inventory, Long quantity) {
        this.inventoryId = inventory.id;
        this.quantity = quantity;
    }
}
