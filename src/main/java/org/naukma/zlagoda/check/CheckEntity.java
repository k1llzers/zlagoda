package org.naukma.zlagoda.check;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.customercard.CustomerCardEntity;
import org.naukma.zlagoda.employee.EmployeeEntity;
import org.naukma.zlagoda.sale.SaleEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckEntity implements GettableById<Integer> {
    private Integer id;
    @NotNull(message = "Employee can't be null.")
    private EmployeeEntity employee;
    private CustomerCardEntity customerCard;
    @NotNull(message = "Print date can't be null.")
    @PastOrPresent(message = "Print date can't be in future.")
    private LocalDateTime printDate;
    @NotNull(message = "Sum total can't be null.")
    @DecimalMin(value="0", message = "Sum total can't be less than zero.")
    private BigDecimal sumTotal;
    @NotNull(message = "Vat can't be null.")
    @DecimalMin(value="0", message = "Vat can't be less than zero.")
    private BigDecimal vat;
    private List<SaleEntity> sales;
}
