package org.naukma.zlagoda.product.dto;

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
public class CreateUpdateProductDto implements GettableById<Integer> {
    private Integer id;
    @NotNull(message = "Category id can't be null.")
    private Integer categoryId;
    @NotNull(message = "Product name can't be null.")
    @NotBlank(message = "Product name can't be blank.")
    @Size(max=50, message = "Product name size can't be more than 50.")
    private String name;
    @NotNull(message = "Product characteristics can't be null.")
    @NotBlank(message = "Product characteristics can't be blank.")
    @Size(max=100, message = "Product characteristics size can't be more than 100.")
    private String characteristics;
}
