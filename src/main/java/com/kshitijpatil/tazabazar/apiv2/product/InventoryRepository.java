package com.kshitijpatil.tazabazar.apiv2.product;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

// workaround for many to many
// ref: https://github.com/spring-projects/spring-data-jdbc/issues/574#issuecomment-877297777
public interface InventoryRepository extends CrudRepository<Inventory, InventoryId> {
    @Query("select * from inventory where (id,product_sku)=(:id,:sku)")
    Optional<Inventory> findByIdAndSku(@Param("id") Long id, @Param("sku") String sku);

    @Override
    default Optional<Inventory> findById(@Param("sku") InventoryId inventory) {
        return findByIdAndSku(inventory.getId(), inventory.getProductSku());
    }

    // TODO: Find a better solution
    @Override
    default Iterable<Inventory> findAllById(Iterable<InventoryId> inventoryIds) {
        var result = new ArrayList<Inventory>();
        inventoryIds.forEach(id -> findById(id).ifPresent(result::add));
        return result;
    }

    @Query("select * from inventory where product_sku=:sku")
    Iterable<Inventory> findAllBySku(@Param("sku") String sku);
}