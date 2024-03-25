package org.naukma.zlagoda.employee;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;

@Mapper(config = MapperConfig.class)
public interface EmployeeMapper {
    EmployeeResponseDto toResponseDto(EmployeeEntity entity);
}
