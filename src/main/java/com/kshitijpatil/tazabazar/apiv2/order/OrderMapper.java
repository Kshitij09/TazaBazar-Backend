package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        var orderLines = order.orderLines.stream()
                .map(OrderMapper::toOrderLineDto)
                .collect(Collectors.toSet());
        return new OrderDto(order.id,
                order.username.getId(),
                order.createdAt,
                order.status,
                orderLines);
    }

    public static OrderLineDto toOrderLineDto(OrderLine orderLine) {
        return new OrderLineDto(orderLine.inventoryId.getId(), orderLine.quantity);
    }
}
