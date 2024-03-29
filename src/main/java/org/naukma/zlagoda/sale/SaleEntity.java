package org.naukma.zlagoda.sale;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleEntity implements GettableById<SaleId> {
    @NotNull(message = "Sale id can't be null")
    private SaleId id;
    @Positive(message = "Product number can't be less than 1")
    private Integer productNumber;
    @DecimalMin(value = "0", message = "Selling price can`t be less than 0")
    private BigDecimal sellingPrice;
}
