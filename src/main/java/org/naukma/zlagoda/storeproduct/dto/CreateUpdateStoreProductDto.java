package org.naukma.zlagoda.storeproduct.dto;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateStoreProductDto implements GettableById<Integer> {
    private Integer id;
    private Integer promotionId;
    private Integer productId;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
    private Boolean promotional;
}
