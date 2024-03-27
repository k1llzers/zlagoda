package org.naukma.zlagoda.product.dto;

import lombok.*;
import org.naukma.zlagoda.category.dto.CategoryResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Integer id;
    private CategoryResponseDto category;
    private String name;
    private String characteristics;
}
