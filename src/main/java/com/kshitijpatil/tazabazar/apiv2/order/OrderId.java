package com.kshitijpatil.tazabazar.apiv2.order;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@AllArgsConstructor
@Value
public class OrderId implements Serializable {
    Long inventoryId;
    String productSku;
}