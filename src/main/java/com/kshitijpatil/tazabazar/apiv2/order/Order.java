package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
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
    @Setter(AccessLevel.PRIVATE)
    public BigDecimal total = BigDecimal.ZERO;
    @MappedCollection(idColumn = "order_id")
    public Set<OrderLine> orderLines = new HashSet<>();

    @PersistenceConstructor
    public Order(UUID id, Instant createdAt, BigDecimal total, String status) {
        this.id = id;
        this.total = total;
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
        total = total.add(orderLine.cost);
        orderLines.add(orderLine);
    }

    public void addAll(OrderLine... orderLines) {
        Arrays.stream(orderLines).forEach(this::add);
    }

    public void addOrderLine(Inventory inventory, Long quantity) {
        var orderLine = createOrderLine(inventory, quantity);
        add(orderLine);
    }
}