package org.naukma.zlagoda.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Category can't be null.")
    private CategoryEntity category;

    @NotNull(message = "Product name can't be null.")
    @NotBlank(message = "Product name can't be blank.")
    @Size(max=50, message = "Product name size can't be more than 50.")
    private String name;
    @NotNull(message = "Product characteristics can't be null.")
    @NotBlank(message = "Product characteristics can't be blank.")
    @Size(max=100, message = "Product characteristics size can't be more than 100.")
    private String characteristics;
}
