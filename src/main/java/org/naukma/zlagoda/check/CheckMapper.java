package org.naukma.zlagoda.check;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.check.dto.CheckResponseDto;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.customercard.CustomerCardMapper;
import org.naukma.zlagoda.employee.EmployeeMapper;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = {EmployeeMapper.class, CustomerCardMapper.class})
public interface CheckMapper {
    CheckResponseDto toResponseDto(CheckEntity entity);
    List<CheckResponseDto> toResponseDtoList(List<CheckEntity> entities);
}
