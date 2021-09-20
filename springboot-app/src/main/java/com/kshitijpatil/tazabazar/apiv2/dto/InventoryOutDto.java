package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InventoryOutDto {
    public Long id;
    public String quantityLabel;
    public BigDecimal price;
    public Instant updatedAt;
    public Integer stockAvailable;
}
