package com.kshitijpatil.tazabazar.apiv2.order;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
    @Query("SELECT username FROM purchase_order WHERE id = :order_id")
    Optional<String> getUsernameById(@Param("order_id") UUID orderId);
}
