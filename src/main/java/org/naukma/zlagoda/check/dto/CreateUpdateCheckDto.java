package org.naukma.zlagoda.check.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateCheckDto implements GettableById<Integer> {
    private Integer id;
    @NotNull(message = "Employee id can't be null.")
    private Integer employeeId; //TODO: in future get from security context
    private Integer customerCardId;
    @NotNull(message = "Sum total can't be null.")
    @DecimalMin(value="0", message = "Sum total can't be less than zero.")
    Map<Integer, Integer> productIdToCountMap;
}
