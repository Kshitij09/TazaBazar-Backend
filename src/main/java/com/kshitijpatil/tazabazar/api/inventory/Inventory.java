package com.kshitijpatil.tazabazar.api.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
public @Data
class Inventory {
    private int id;
    private int quantity;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("modified_at")
    private OffsetDateTime modifiedAt;
}