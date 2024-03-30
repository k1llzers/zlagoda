package org.naukma.zlagoda.product;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.category.CategoryMapper;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.product.dto.ProductResponseDto;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = {CategoryMapper.class})
public interface ProductMapper {
    ProductResponseDto toResponseDto(ProductEntity entity);

    List<ProductResponseDto> toResponseDtoList(List<ProductEntity> entity);
}
