package org.naukma.zlagoda.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDto {
    private String name;
    private Integer productNumber;
    private BigDecimal sellingPrice;
}
