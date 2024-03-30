package org.naukma.zlagoda.employee;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.employee.dto.CreateUpdateEmployeeDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService extends BaseService<CreateUpdateEmployeeDto, EmployeeEntity, Integer> {
    private final EmployeeMapper mapper;
    public EmployeeService(EmployeeMapper mapper) {
        super(EmployeeEntity::new, EmployeeEntity.class);
        this.mapper = mapper;
    }

    public EmployeeResponseDto getEmployeeResponseDto(Integer id) {
        return mapper.toResponseDto(getById(id));
    }

    public List<EmployeeResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<EmployeeResponseDto> getAllOrderBySurname() {
        return mapper.toResponseDtoList(repository.findAllOrderBy());
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
