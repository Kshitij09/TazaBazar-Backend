package com.kshitijpatil.tazabazar.apiv2.order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, OrderId> {
}
