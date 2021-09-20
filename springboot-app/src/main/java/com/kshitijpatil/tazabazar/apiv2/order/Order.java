package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.product.Inventory;
import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.*;

@Data
@Table("purchase_order")
public class Order {
    @Id
    public final UUID id;
    public final AggregateReference<User, String> username;
    public Instant createdAt;
    public OrderStatus status;
    @MappedCollection(idColumn = "order_id")
    public Set<OrderLine> orderLines = new HashSet<>();

    @PersistenceConstructor
    public Order(UUID id, AggregateReference<User, String> username, Instant createdAt, OrderStatus status) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Order(AggregateReference<User, String> username, Instant createdAt, OrderStatus status) {
        this.id = null;
        this.username = username;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static OrderLine createOrderLine(Inventory inventory, Long quantity) {
        return new OrderLine(AggregateReference.to(inventory.id), quantity);
    }

    public void add(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    public void addAll(OrderLine... orderLines) {
        Arrays.stream(orderLines).forEach(this::add);
    }

    public void addAll(Collection<OrderLine> orderLines) {
        orderLines.forEach(this::add);
    }
}