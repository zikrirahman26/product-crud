package com.example.product.controller;

import com.example.product.dto.ApiResponse;
import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductListResponse;
import com.example.product.dto.ProductResponse;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addProduct(@RequestBody ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .message("Product added")
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .message("Product updated")
                        .build());
    }

    @GetMapping
    public ResponseEntity<List<ProductListResponse>> listProducts() {
        List<ProductListResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .message("Product deleted")
                        .build());
    }
}
