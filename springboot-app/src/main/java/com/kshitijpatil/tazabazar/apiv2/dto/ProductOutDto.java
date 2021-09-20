package com.kshitijpatil.tazabazar.apiv2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductOutDto {
    public String sku;
    public String name;
    public String category;
    public Set<InventoryOutDto> inventories = new HashSet<>();
}
