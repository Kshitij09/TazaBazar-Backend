package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.apiv2.dto.InventoryOutDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductCategoryDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import com.kshitijpatil.tazabazar.apiv2.product.IProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Products (DB)")
@RestController
@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
public class ProductControllerV2 {
    private final IProductService productService;

    @GetMapping
    public List<ProductOutDto> getProducts(@RequestParam(value = "category", required = false) String category,
                                           @RequestParam(value = "q", required = false) String query) {
        return productService.getProductsByCategoryAndName(category, query);
    }

    @GetMapping("categories")
    public List<ProductCategoryDto> getProductCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("{product_sku}")
    public ProductOutDto getProductBySku(@PathVariable("product_sku") String productSku) {
        return productService.getProductBySku(productSku);
    }

    @GetMapping("{product_sku}/inventories")
    public List<InventoryOutDto> getProductInventories(@PathVariable("product_sku") String productSku) {
        return productService.getProductInventoriesBySku(productSku);
    }
}
