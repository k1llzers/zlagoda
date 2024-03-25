package org.naukma.zlagoda.category;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity implements GettableById<Integer> {
    private Integer id;
    private String name;
}
