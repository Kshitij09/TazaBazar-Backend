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

    @Query("SELECT * FROM product where name @@ to_tsquery(:query)")
    List<Product> searchProductByName(@Param("query") String query);

    @Query("SELECT * FROM product where name @@ to_tsquery(:nameQuery) and category = :category")
    List<Product> searchProductByCategoryAndName(@Param("category") String category, @Param("nameQuery") String query);
}
