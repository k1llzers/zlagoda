package org.naukma.zlagoda.employee;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.employee.dto.CreateUpdateEmployeeDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends BaseService<CreateUpdateEmployeeDto, EmployeeEntity, Integer> {
    public EmployeeService() {
        super(EmployeeEntity::new, EmployeeEntity.class);
    }

    public EmployeeResponseDto getEmployeeResponseDto(Integer id) {
        EmployeeEntity entity = getById(id);
        return EmployeeResponseDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .surname(entity.getSurname())
                .name(entity.getName())
                .patronymic(entity.getPatronymic())
                .dateOfBirth(entity.getDateOfBirth())
                .dateOfStart(entity.getDateOfStart())
                .phoneNumber(entity.getPhoneNumber())
                .city(entity.getCity())
                .street(entity.getStreet())
                .zipCode(entity.getZipCode())
                .build();
    }

    @Override
    protected void mergeEntity(EmployeeEntity entity, CreateUpdateEmployeeDto dto) {
        if(dto.getId() != null)
            entity.setId(dto.getId());
        if(dto.getLogin() != null)
            entity.setLogin(dto.getLogin());
        if(dto.getPassword() != null)
            entity.setPassword(dto.getPassword());
        if(dto.getSurname() != null)
            entity.setSurname(dto.getSurname());
        if(dto.getName() != null)
            entity.setName(dto.getName());
        if(dto.getPatronymic() != null)
            entity.setPatronymic(dto.getPatronymic());
        if(dto.getRole() != null)
            entity.setRole(dto.getRole());
        if(dto.getSalary() != null)
            entity.setSalary(dto.getSalary());
        if(dto.getDateOfBirth() != null)
            entity.setDateOfBirth(dto.getDateOfBirth());
        if(dto.getDateOfStart() != null)
            entity.setDateOfStart(dto.getDateOfStart());
        if(dto.getPhoneNumber() != null)
            entity.setPhoneNumber(dto.getPhoneNumber());
        if(dto.getCity() != null)
            entity.setCity(dto.getCity());
        if(dto.getStreet() != null)
            entity.setStreet(dto.getStreet());
        if(dto.getZipCode() != null)
            entity.setZipCode(dto.getZipCode());
    }
}
