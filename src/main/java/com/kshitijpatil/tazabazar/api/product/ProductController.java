package com.kshitijpatil.tazabazar.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    @Qualifier("in_memory")
    ProductService productService;

    @GetMapping
    public List<ProductOutDto> getProducts() {
        return productService.getAllProducts();
    }
}
