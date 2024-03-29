package org.naukma.zlagoda.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity implements GettableById<Integer> {
    private Integer id;
    @NotNull(message = "Category name can't be null.")
    @NotBlank(message = "Category name can't be blank.")
    @Size(max = 50, message = "Category name size can't be more than 50.")
    private String name;
}
