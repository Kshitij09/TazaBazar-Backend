package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@AllArgsConstructor
@Value
public class InventoryId implements Serializable {
    @Id
    Long id;
    String productSku;
}
