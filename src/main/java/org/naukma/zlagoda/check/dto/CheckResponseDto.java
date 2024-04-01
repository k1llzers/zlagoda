package org.naukma.zlagoda.check.dto;

import lombok.*;
import org.naukma.zlagoda.customercard.dto.CustomerCardResponseDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;
import org.naukma.zlagoda.sale.dto.SaleResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResponseDto {
    private Integer id;
    private EmployeeResponseDto employee;
    private CustomerCardResponseDto customerCard;
    private LocalDateTime printDate;
    private BigDecimal sumTotal;
    private BigDecimal vat;
    private List<SaleResponseDto> sales;
}
