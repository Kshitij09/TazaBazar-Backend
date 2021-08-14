package com.kshitijpatil.tazabazar.api.product;

public class ProductMapper {
    public static ProductOutDto toProductOutDto(Product product) {
        var outDto = new ProductOutDto();
        outDto.setProductId(product.getProductId());
        outDto.setSku(product.getSku());
        outDto.setCategoryId(product.getProductCategory().ordinal());
        outDto.setInventoryId(product.getProductInventory().getId());
        outDto.setPrice(product.getPrice());
        outDto.setName(product.getName());
        outDto.setImageUri(product.getImageUri());
        outDto.setQuantityLabel(product.getQuantityLabel());
        return outDto;
    }
}
