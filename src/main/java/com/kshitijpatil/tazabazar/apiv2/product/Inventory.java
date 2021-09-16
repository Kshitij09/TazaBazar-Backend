package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Inventory {
    @Transient
    private static final int PRICE_SCALE = 2;
    @Id
    public Long id;
    public AggregateReference<Product, String> productSku;
    public String quantityLabel;
    public BigDecimal price;
    public Instant updatedAt;
    public Integer stockAvailable;

    @PersistenceConstructor
    public Inventory(Long id, AggregateReference<Product, String> productSku, String quantityLabel, BigDecimal price, Instant updatedAt, Integer stockAvailable) {
        this.id = id;
        this.productSku = productSku;
        this.quantityLabel = quantityLabel;
        this.price = price;
        this.updatedAt = updatedAt;
        this.stockAvailable = stockAvailable;
    }

    public Inventory(String quantityLabel, double price, Instant updatedAt, Integer stockAvailable) {
        this.quantityLabel = quantityLabel;
        this.price = BigDecimal.valueOf(price);
        this.updatedAt = updatedAt;
        this.stockAvailable = stockAvailable;
    }
}
