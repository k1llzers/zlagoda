package org.naukma.zlagoda.storeproduct;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Product can't be null.")
    private ProductEntity product;
    @NotNull(message = "Selling price can't be null.")
    @DecimalMin(value = "0", message = "Selling price can't be less than zero.")
    private BigDecimal sellingPrice;
    @NotNull(message = "Products number can't be null.")
    @Min(value = 0, message = "Products number can't be less than zero.")
    private Integer productsNumber;
    @NotNull(message = "Promotional can't be null.")
    private boolean promotional = false;
}
