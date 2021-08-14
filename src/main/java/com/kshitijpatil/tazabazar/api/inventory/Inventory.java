package com.kshitijpatil.tazabazar.api.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
public @Data
class Inventory {
    private int id;
    private int quantity;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
}
