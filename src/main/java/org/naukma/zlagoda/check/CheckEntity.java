package org.naukma.zlagoda.check;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.customercard.CustomerCardEntity;
import org.naukma.zlagoda.employee.EmployeeEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckEntity implements GettableById<Integer> {
    private Integer id;
    private EmployeeEntity employee;
    private CustomerCardEntity customerCard;
    private LocalDateTime printDate;
    private BigDecimal sumTotal;
    private BigDecimal vat;
}
