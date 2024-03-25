package org.naukma.zlagoda.category.dto;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.category.CategoryEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateCategoryDto implements GettableById<Integer> {
    private Integer id;
    private String name;
}
