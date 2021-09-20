package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Data
@AllArgsConstructor(onConstructor_ = @PersistenceConstructor)
public class Inventory {
    @Id
    public Long id;
    public AggregateReference<Product, String> productSku;
    public String quantityLabel;
    public BigDecimal price;
    public Instant updatedAt;
    public Integer stockAvailable;

    public Inventory(String quantityLabel, double price, Instant updatedAt, Integer stockAvailable) {
        this.quantityLabel = quantityLabel;
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_EVEN);
        this.updatedAt = updatedAt;
        this.stockAvailable = stockAvailable;
    }
}
