package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@AllArgsConstructor
public class Product {
    @Id
    public String sku;
    @NotBlank
    public String name;
    public AggregateReference<ProductCategory, String> category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }
}
