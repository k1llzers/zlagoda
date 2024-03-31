package org.naukma.zlagoda.storeproduct.dto;

import lombok.*;
import org.naukma.zlagoda.product.dto.ProductResponseDto;
import org.naukma.zlagoda.product.dto.ProductWithoutCategoryDto;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductShortResponseDto {
    private Integer id;
    private ProductWithoutCategoryDto product;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
}
