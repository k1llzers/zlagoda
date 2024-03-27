package org.naukma.zlagoda.product;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.category.CategoryEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity implements GettableById<Integer> {
    private Integer id;
    private CategoryEntity category;
    private String name;
    private String characteristics;
}
