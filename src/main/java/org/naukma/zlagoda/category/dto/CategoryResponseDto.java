package org.naukma.zlagoda.category.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Integer id;
    private String name;
}
