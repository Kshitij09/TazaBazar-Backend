package com.kshitijpatil.tazabazar.api.product;

public class ProductMapper {
    public static ProductOutDto toProductOutDto(Product product) {
        var outDto = new ProductOutDto();
        outDto.setProductId(product.getProductId());
        outDto.setSku(product.getSku());
        outDto.setCategoryId(product.getProductCategory().ordinal());
        outDto.setInventory(product.getProductInventory());
        outDto.setPrice(product.getPrice());
        outDto.setName(product.getName());
        outDto.setImageUri(product.getImageUri());
        outDto.setQuantityLabel(product.getQuantityLabel());
        return outDto;
    }

    public static Product fromProductDto(ProductOutDto productDto) {
        var product = new Product();
        // we silently ignore the read only property 'productId'
        product.setProductInventory(productDto.getInventory());
        var category = ProductCategory.values()[productDto.getCategoryId()];
        product.setProductCategory(category);
        product.setSku(productDto.getSku());
        product.setPrice(productDto.getPrice());
        product.setImageUri(productDto.getImageUri());
        product.setName(productDto.getName());
        product.setQuantityLabel(productDto.getQuantityLabel());
        return product;
    }
}
