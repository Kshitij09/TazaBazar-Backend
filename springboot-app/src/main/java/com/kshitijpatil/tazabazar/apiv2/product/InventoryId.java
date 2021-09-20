package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@AllArgsConstructor
@Value
public class InventoryId implements Serializable {
    Long id;
    String productSku;
}
