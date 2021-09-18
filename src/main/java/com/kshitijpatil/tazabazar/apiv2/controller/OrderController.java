package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;
import com.kshitijpatil.tazabazar.apiv2.order.IOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/orders")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final IOrderService orderService;

    @PostMapping
    public OrderDto placeOrder(@RequestBody @NotEmpty(message = "Order Lines cannot be empty.") List<@Valid OrderLineDto> orderLines,
                               Principal principal) {
        return orderService.placeOrder(principal.getName(), orderLines);
    }

    @GetMapping("{order_id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("order_id") UUID orderId, Principal principal) {
        var creator = orderService.getOrderCreatorById(orderId);
        if (!principal.getName().equals(creator))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
