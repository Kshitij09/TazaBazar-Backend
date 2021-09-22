package com.kshitijpatil.tazabazar.apiv2.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.util.*;

public class Product {
    @Id
    public String sku;
    @NotBlank
    public String name;
    public AggregateReference<ProductCategory, String> category;
    public String imageUri;
    @MappedCollection(idColumn = "product_sku")
    public Set<Inventory> inventories = new HashSet<>();

    public Product(String sku, String name, AggregateReference<ProductCategory, String> category) {
        this.sku = sku;
        this.name = name;
        this.category = category;
    }

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

    public void add(Inventory inventory) {
        Assert.notNull(inventory, "Inventory should not be null");
        inventory.productSku = AggregateReference.to(this.sku);
        inventories.add(inventory);
    }

    public void addAll(Collection<Inventory> inventories) {
        inventories.forEach(this::add);
    }

    public void addAll(Inventory... inventories) {
        Arrays.stream(inventories).forEach(this::add);
    }
}
