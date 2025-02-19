package com.example.product.service;

import com.example.product.dto.CategoryListResponse;
import com.example.product.dto.CategoryProductResponse;
import com.example.product.dto.CategoryRequest;
import com.example.product.model.Category;
import com.example.product.model.Product;
import com.example.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidationService validationService;

    public void addCategory(CategoryRequest categoryRequest) {
        validationService.validate(categoryRequest);

        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);
    }

    public List<CategoryListResponse> listAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::categoryListResponse).toList();
    }

    private CategoryListResponse categoryListResponse(Category category) {
        List<CategoryProductResponse> categoryProductResponses = category.getProducts().stream()
                .map(this::productResponse)
                .toList();

        return CategoryListResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .products(categoryProductResponses)
                .build();
    }

    private CategoryProductResponse productResponse(Product product) {
        return CategoryProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .build();
    }
}
