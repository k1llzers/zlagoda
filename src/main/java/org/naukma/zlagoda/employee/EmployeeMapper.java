package org.naukma.zlagoda.employee;

import org.mapstruct.Mapper;
import org.naukma.zlagoda.config.MapperConfig;
import org.naukma.zlagoda.employee.dto.EmployeePhoneNumberAddressDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface EmployeeMapper {
    EmployeeResponseDto toResponseDto(EmployeeEntity entity);
    List<EmployeeResponseDto> toResponseDtoList(List<EmployeeEntity> entities);

    List<EmployeePhoneNumberAddressDto> toPhoneNumberAddressDtoList(List<EmployeeEntity> entities);
}
