package org.naukma.zlagoda.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.SneakyThrows;
import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.employee.dto.CreateUpdateEmployeeDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class EmployeeService extends BaseService<CreateUpdateEmployeeDto, EmployeeEntity, Integer> {
    private final EmployeeMapper mapper;
    private final ObjectMapper objectMapper;

    public EmployeeService(EmployeeMapper mapper, ObjectMapper objectMapper) {
        super(EmployeeEntity::new, "employee");
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    public EmployeeResponseDto getEmployeeResponseDto(Integer id) {
        return mapper.toResponseDto(getById(id));
    }

    public List<EmployeeResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<EmployeeResponseDto> getAllOrderBySurname() {
        return mapper.toResponseDtoList(repository.findAllOrderByDefault());
    }

    public List<EmployeeResponseDto> getCashiersWhoServedAllCustomers() {
        return mapper.toResponseDtoList(((EmployeeRepository)repository).findCashiersWhoServedAllCustomers());
    }

    public List<EmployeeResponseDto> getPhoneNumberAndAddressBySurname(String surname) {
        return mapper.toResponseDtoList(((EmployeeRepository) repository).findPhoneNumberAndAddressBySurname(surname));
    }

    public List<EmployeeResponseDto> getAllCashiersOrderBySurname() {
        return mapper.toResponseDtoList(((EmployeeRepository)repository).findAllCashiersOrderedBySurname());
    }

    public EmployeeResponseDto getMyselfInfo() {
        return mapper.toResponseDto((EmployeeEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Override
    public Boolean delete(Integer id) {
        if (((EmployeeEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(id))
            throw new ValidationException("You can't delete yourself!");
        return repository.delete(id);
    }

    @SneakyThrows
    public Boolean changePassword(String password) {
        if (password.isBlank()) throw new ValidationException("Password can`t be blank");
        HashMap<String, String> hashMap = objectMapper.readValue(password, HashMap.class);
        return ((EmployeeRepository) repository).changePassword(hashMap.get("password"));
    }

    @Override
    protected void mergeEntity(EmployeeEntity entity, CreateUpdateEmployeeDto dto) {
        if(dto.getId() != null)
            entity.setId(dto.getId());
        if(dto.getLogin() != null)
            entity.setLogin(dto.getLogin());
        if(dto.getSurname() != null)
            entity.setSurname(dto.getSurname());
        if(dto.getName() != null)
            entity.setName(dto.getName());
        entity.setPatronymic(dto.getPatronymic());
        if (dto.getPatronymic() != null && dto.getPatronymic().isBlank())
            throw new ValidationException("Employee patronymic can't be blank");
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
