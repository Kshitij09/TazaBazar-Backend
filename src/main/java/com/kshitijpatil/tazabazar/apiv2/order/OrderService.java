package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;
import com.kshitijpatil.tazabazar.apiv2.product.InventoryNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.product.InventoryRepository;
import com.kshitijpatil.tazabazar.apiv2.userdetail.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.kshitijpatil.tazabazar.utils.ExceptionUtils.usernameNotFoundExceptionSupplier;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orders;
    private final UserRepository users;
    private final InventoryRepository inventories;

    @Override
    public OrderDto placeOrder(String placeBy, List<OrderLineDto> orderLines) {
        var user = users.findById(placeBy)
                .orElseThrow(usernameNotFoundExceptionSupplier(placeBy));
        final Order order = new Order(AggregateReference.to(user.username), Instant.now(), OrderStatus.ACCEPTED);
        orderLines.forEach(orderLineDto -> {
            var inventory = inventories.findById(orderLineDto.inventoryId)
                    .orElseThrow(() -> new InventoryNotFoundException(orderLineDto.inventoryId));
            order.add(Order.createOrderLine(inventory, orderLineDto.quantity));
        });
        var saved = orders.save(order);
        var savedOrderLines = saved.orderLines.stream()
                .map(orderLine -> new OrderLineDto(orderLine.inventoryId.getId(), orderLine.quantity))
                .collect(Collectors.toSet());
        return new OrderDto(saved.id, saved.username.getId(), saved.createdAt, saved.status, savedOrderLines);
    }

    @Override
    public String getOrderCreatorById(UUID orderId) throws OrderNotFoundException {
        return orders.getUsernameById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
    }

    @Override
    public OrderDto getOrderById(UUID orderId) throws OrderNotFoundException {
        return orders.findById(orderId)
                .map(OrderMapper::toOrderDto)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
    }

    @Override
    public List<OrderDto> getOrdersByUsername(String username) {
        return StreamSupport.stream(orders.findOrdersByUsername(username).spliterator(), false)
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());
    }
}
