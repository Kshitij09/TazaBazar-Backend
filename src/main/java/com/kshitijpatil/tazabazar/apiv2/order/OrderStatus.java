package com.kshitijpatil.tazabazar.apiv2.order;

import lombok.Getter;

public enum OrderStatus {
    ACCEPTED("Accepted"),
    PENDING("Pending"),
    DISPATCHED("Dispatched"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    @Getter
    private final String label;

    private OrderStatus(String label) {
        this.label = label;
    }
}
