package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kshitijpatil.tazabazar.apiv2.order.OrderStatus;
import com.kshitijpatil.tazabazar.apiv2.userdetail.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDto {
    public UUID id;
    public Instant createdAt;
    public OrderStatus status;
    public Set<OrderLineDto> orderLines = new HashSet<>();

    public OrderDto(AggregateReference<User, String> username, Instant createdAt, OrderStatus status) {
        this.id = null;
        this.createdAt = createdAt;
        this.status = status;
    }

    public void add(OrderLineDto orderLine) {
        orderLines.add(orderLine);
    }

    public void addAll(OrderLineDto... orderLines) {
        Arrays.stream(orderLines).forEach(this::add);
    }
}