package org.naukma.zlagoda.storeproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.product.dto.ProductWithoutCategoryDto;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductWithPriceCountNameAndCharacteristicDto {
    private Integer id;
    private ProductWithoutCategoryDto product;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
}
