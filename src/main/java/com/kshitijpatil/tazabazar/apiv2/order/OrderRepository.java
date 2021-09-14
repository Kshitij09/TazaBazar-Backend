package com.kshitijpatil.tazabazar.apiv2.order;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
}
