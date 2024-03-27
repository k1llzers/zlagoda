package org.naukma.zlagoda.storeproduct;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.product.ProductMapper;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;

@Mapper(config = MapperConfig.class, uses = {ProductMapper.class})
public interface StoreProductMapper {
    StoreProductResponseDto toResponseDto(StoreProductEntity entity);
}
