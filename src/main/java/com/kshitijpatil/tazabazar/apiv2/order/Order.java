package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Table("purchase_order")
public class Order {
    @Id
    public final UUID id;
    public Instant createdAt;
    public String status;
    @MappedCollection(idColumn = "order_id")
    public Set<OrderLine> orderLines = new HashSet<>();

    @PersistenceConstructor
    public Order(UUID id, Instant createdAt, String status) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Order(Instant createdAt, String status) {
        this.id = null;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static OrderLine createOrderLine(Inventory inventory, Long quantity) {
        return new OrderLine(inventory, quantity);
    }

    public void add(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    public void addAll(OrderLine... orderLines) {
        Arrays.stream(orderLines).forEach(this::add);
    }
}