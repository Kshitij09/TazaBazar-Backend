package com.kshitijpatil.tazabazar.apiv2.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDto {
    public Long inventoryId;
    @Positive
    @Max(8)
    @EqualsAndHashCode.Exclude
    public Long quantity;
}
