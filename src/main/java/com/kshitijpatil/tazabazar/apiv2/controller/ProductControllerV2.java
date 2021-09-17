package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import com.kshitijpatil.tazabazar.apiv2.product.IProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Products (DB)")
@RestController
@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
public class ProductControllerV2 {
    private final IProductService productService;

    @GetMapping
    public List<ProductOutDto> getProducts(@RequestParam(value = "category", required = false) String category) {
        if (category == null) {
            return productService.getAllProducts();
        } else {
            return productService.getProductsByCategory(category);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
