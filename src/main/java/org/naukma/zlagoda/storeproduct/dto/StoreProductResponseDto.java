package org.naukma.zlagoda.storeproduct.dto;

import lombok.*;
import org.naukma.zlagoda.product.dto.ProductResponseDto;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductResponseDto {
    private Integer id;
    private StoreProductResponseDto promotion;
    private ProductResponseDto product;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
    private Boolean promotional;
}
