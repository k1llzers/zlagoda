package org.naukma.zlagoda.product.dto;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateProductDto implements GettableById<Integer> {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String characteristics;
}
