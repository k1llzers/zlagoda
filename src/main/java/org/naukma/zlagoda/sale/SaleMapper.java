package org.naukma.zlagoda.sale;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.sale.dto.SaleResponseDto;

@Mapper(config = MapperConfig.class)
public interface SaleMapper {
    @Mapping(target = "name", expression = "java(entity.getId().getStoreProduct().getProduct().getName())")
    @Mapping(target = "upc", expression = "java(entity.getId().getStoreProduct().getId())")
    SaleResponseDto toResponseDto(SaleEntity entity);
}
