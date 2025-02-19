package com.example.product.controller;

import com.example.product.dto.CategoryListResponse;
import com.example.product.dto.CategoryRequest;
import com.example.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return new ResponseEntity<>("Category added", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> categoryList() {
        List<CategoryListResponse> categoryListResponses = categoryService.listAllCategories();
        return new ResponseEntity<>(categoryListResponses, HttpStatus.OK);
    }
}
