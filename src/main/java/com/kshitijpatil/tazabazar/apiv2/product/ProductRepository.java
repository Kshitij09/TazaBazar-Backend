package com.kshitijpatil.tazabazar.apiv2.product;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {
    @Query("SELECT * FROM product WHERE category = :category")
    List<Product> findByCategory(@Param("category") String category);

    @Query("SELECT exists (SELECT true FROM product where sku = :sku)")
    boolean skuExists(@Param("sku") String productSku);
}
