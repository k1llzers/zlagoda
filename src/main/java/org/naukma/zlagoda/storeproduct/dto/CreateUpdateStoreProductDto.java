package org.naukma.zlagoda.storeproduct.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Product id can't be null.")
    private Integer productId;
    @NotNull(message = "Selling price can't be null.")
    @DecimalMin(value = "0", message = "Selling price can't be less than zero.")
    private BigDecimal sellingPrice;
    @NotNull(message = "Products number can't be null.")
    @Min(value = 0, message = "Products number can't be less than zero.")
    private Integer productsNumber;
    @NotNull(message = "Promotional can't be null.")
    private Boolean promotional;
}
