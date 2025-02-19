package com.example.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @Pattern(regexp = "^[A-Z]{3}\\d{5}$")
    private String code;

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    private Long categoryId;
}
