package com.kshitijpatil.tazabazar.apiv2.order;

import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;

import javax.validation.Valid;
import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(String placeBy, List<@Valid OrderLineDto> orderLines);
}
