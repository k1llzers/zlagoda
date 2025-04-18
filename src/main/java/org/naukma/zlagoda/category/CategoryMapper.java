package org.naukma.zlagoda.category;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.category.dto.CategoryResponseDto;
import org.naukma.zlagoda.config.MapperConfig;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toResponseDto(CategoryEntity entity);

    List<CategoryResponseDto> toResponseDtoList(List<CategoryEntity> entities);
}
