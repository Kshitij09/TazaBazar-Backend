package com.kshitijpatil.tazabazar.api.product;

import com.kshitijpatil.tazabazar.api.inventory.InventoryDto;
import com.kshitijpatil.tazabazar.api.inventory.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    @Qualifier("in_memory_product")
    ProductService productService;

    @Autowired
    @Qualifier("in_memory_inventory")
    InventoryService inventoryService;

    @GetMapping
    public List<ProductOutDto> getProducts(@RequestParam(value = "category_id", required = false) Integer categoryId) {
        if (categoryId == null) {
            return productService.getAllProducts();
        } else {
            return productService.getProductsByCategoryId(categoryId);
        }
    }

    @GetMapping("/{product_id}")
    public ProductOutDto getProductById(@PathVariable("product_id") int productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/{product_id}")
    public void updateProduct(@RequestBody ProductOutDto productDto, @PathVariable("product_id") int productId) {
        productService.updateProduct(productId, productDto);
    }

    @DeleteMapping("/{product_id}")
    public void deleteProduct(@PathVariable("product_id") int productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getProductCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/{product_id}/inventory")
    public InventoryDto getInventoryById(@PathVariable("product_id") int productId) {
        return inventoryService.getInventoryById(productId);
    }
}
