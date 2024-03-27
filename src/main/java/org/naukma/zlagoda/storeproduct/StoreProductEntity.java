package org.naukma.zlagoda.storeproduct;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.product.ProductEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductEntity implements GettableById<Integer> {
    private Integer id;
    private StoreProductEntity promotion;
    private ProductEntity product;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
    private boolean promotional = false;
}
