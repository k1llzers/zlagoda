package org.naukma.zlagoda.storeproduct;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.product.ProductMapper;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductShortResponseDto;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = {ProductMapper.class})
public interface StoreProductMapper {
    StoreProductResponseDto toResponseDto(StoreProductEntity entity);
    List<StoreProductResponseDto> toResponseDtoList(List<StoreProductEntity> entity);

    List<StoreProductShortResponseDto> toShortResponseDtoList(List<StoreProductEntity> entities);
}
