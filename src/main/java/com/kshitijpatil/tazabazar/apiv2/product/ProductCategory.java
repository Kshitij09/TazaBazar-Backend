package com.kshitijpatil.tazabazar.apiv2.product;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Objects;


@AllArgsConstructor
public class ProductCategory {
    @Id
    public String label;
    public String skuPrefix;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
