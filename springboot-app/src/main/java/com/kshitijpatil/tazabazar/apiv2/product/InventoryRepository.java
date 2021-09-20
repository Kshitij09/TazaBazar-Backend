package com.kshitijpatil.tazabazar.apiv2.product;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// workaround for many to many
// ref: https://github.com/spring-projects/spring-data-jdbc/issues/574#issuecomment-877297777
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    @Query("select * from inventory where product_sku=:sku")
    Iterable<Inventory> findAllBySku(@Param("sku") String sku);
}