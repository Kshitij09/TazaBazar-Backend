package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
    OrderDto placeOrder(String placeBy, List<@Valid OrderLineDto> orderLines);

    String getOrderCreatorById(UUID orderId) throws OrderNotFoundException;

    OrderDto getOrderById(UUID orderId) throws OrderNotFoundException;
}
