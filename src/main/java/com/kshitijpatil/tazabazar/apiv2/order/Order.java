package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Table("purchase_order")
public class Order {
    @Id
    UUID id;
    Instant createdAt;
    String status;
    @Transient
    BigDecimal total = BigDecimal.ZERO;
    @MappedCollection(idColumn = "order_id")
    Set<OrderLine> orderLines = new HashSet<>();

    @PersistenceConstructor
    public Order(UUID id, Instant createdAt, String status) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Order(Instant createdAt, String status) {
        this.createdAt = createdAt;
        this.status = status;
    }

    public static OrderLine createOrderLine(Inventory inventory, Long quantity) {
        return new OrderLine(inventory.id, inventory.productSku.getId(), quantity);
    }

    public void addOrderLine(Inventory inventory, Long quantity) {
        var orderLine = createOrderLine(inventory, quantity);
        var cost = inventory.price.multiply(BigDecimal.valueOf(quantity));
        total = total.add(cost);
        orderLines.add(orderLine);
    }
}