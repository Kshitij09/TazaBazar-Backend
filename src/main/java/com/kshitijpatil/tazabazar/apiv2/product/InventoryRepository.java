package com.kshitijpatil.tazabazar.apiv2.product;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// workaround for many to many
// ref: https://github.com/spring-projects/spring-data-jdbc/issues/574#issuecomment-877297777
// NOTE: findById, findAllById WON"T WORK
public interface InventoryRepository extends CrudRepository<Inventory, InventoryId> {
    //    @Override
//    @Query("select * from inventory where (id, product_sku)=(:inventoryId.id,:inventoryId.productSku)")
//    Optional<Inventory> findById(@Param("inventoryId") InventoryId inventoryId);
    @Query("select * from inventory where (id,product_sku)=(:id,:sku)")
    Optional<Inventory> findByIdAndSku(@Param("id") Long id, @Param("sku") String sku);

    @Query("select * from inventory where product_sku=:sku")
    Iterable<Inventory> findAllBySku(@Param("sku") String sku);
}