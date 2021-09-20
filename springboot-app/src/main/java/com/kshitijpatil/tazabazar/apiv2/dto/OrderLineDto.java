package com.kshitijpatil.tazabazar.apiv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderLineDto {
    public Long inventoryId;
    @Positive
    @Max(8)
    public Long quantity;
}
