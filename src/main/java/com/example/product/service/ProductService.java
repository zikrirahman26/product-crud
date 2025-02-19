package com.example.product.service;

import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductListResponse;
import com.example.product.dto.ProductResponse;
import com.example.product.model.Category;
import com.example.product.model.Product;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ValidationService validationService;

    public void addProduct(ProductDTO productDTO) {
        validationService.validate(productDTO);

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        String productCode = productDTO.getCode();
        if (productCode == null || productCode.isEmpty()) {
            productCode = generateNewCode();
        } else if (productRepository.findByCode(productCode).isPresent()) {
            productCode = incrementCode(productCode);
        }

        Product product = new Product();
        product.setCode(productCode);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);
        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductDTO productDTO) {
        validationService.validate(productDTO);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        product.setCode(productDTO.getCode());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);
        productRepository.save(product);
    }

    public List<ProductListResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::productListResponse).toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return productResponse(product);
    }

    public void deleteProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    private ProductResponse productResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .categoryDesc(product.getCategoryDesc())
                .build();
    }

    private ProductListResponse productListResponse(Product product) {
        return ProductListResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .categoryDesc(product.getCategoryDesc())
                .build();
    }

    private String generateNewCode() {
        String prefix = "PRD";
        Optional<Product> lastProduct = productRepository.findTopByOrderByCodeDesc();

        if (lastProduct.isEmpty() || lastProduct.get().getCode() == null) {
            return prefix + "00001";
        }
        return incrementCode(lastProduct.get().getCode());
    }

    private String incrementCode(String code) {
        String prefix = code.replaceAll("[0-9]", "");
        String numberPart = code.replaceAll("[^0-9]", "");

        int number = numberPart.isEmpty() ? 0 : Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%05d", number);
    }
}