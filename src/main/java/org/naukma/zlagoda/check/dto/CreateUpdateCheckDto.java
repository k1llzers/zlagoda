package org.naukma.zlagoda.check.dto;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateCheckDto implements GettableById<Integer> {
    private Integer id;
    private Integer employeeId; //TODO: in future get from security context
    private Integer customerCardId;
    private LocalDateTime printDate;
    private BigDecimal sumTotal;
    private BigDecimal vat;
}
