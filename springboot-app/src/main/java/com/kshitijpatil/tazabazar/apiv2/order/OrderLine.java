package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Value
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
public class OrderLine {
    public AggregateReference<Inventory, Long> inventoryId;
    @Positive
    @Max(8)
    @EqualsAndHashCode.Exclude
    public Long quantity;

    public OrderLine(Inventory inventory, Long quantity) {
        this.inventoryId = AggregateReference.to(inventory.id);
        this.quantity = quantity;
    }
}
