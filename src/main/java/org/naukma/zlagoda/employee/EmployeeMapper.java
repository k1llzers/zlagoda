package org.naukma.zlagoda.employee;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.employee.dto.CreateUpdateEmployeeDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;

@Mapper(config = MapperConfig.class, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployeeMapper {
    EmployeeResponseDto toResponseDto(EmployeeEntity entity);

    EmployeeEntity toEntity(CreateUpdateEmployeeDto dto);
}
