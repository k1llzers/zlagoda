package org.naukma.zlagoda.customercard;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.customercard.dto.CustomerCardResponseDto;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface CustomerCardMapper {
    CustomerCardResponseDto toResponseDto(CustomerCardEntity entity);
    List<CustomerCardResponseDto> toResponseDtoList(List<CustomerCardEntity> entity);
}
