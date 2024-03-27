package org.naukma.zlagoda.check;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.check.dto.CheckResponseDto;
import org.naukma.zlagoda.check.dto.CreateUpdateCheckDto;
import org.naukma.zlagoda.customercard.CustomerCardService;
import org.naukma.zlagoda.employee.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class CheckService extends BaseService<CreateUpdateCheckDto, CheckEntity, Integer> {

    private final CheckMapper mapper;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;

    public CheckService(CheckMapper mapper, EmployeeService employeeService, CustomerCardService customerCardService) {
        super(CheckEntity::new, CheckEntity.class);
        this.mapper = mapper;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;
    }

    public CheckResponseDto getCheckResponseDto(Integer id) {
        return mapper.toResponseDto(getById(id));
    }

    @Override
    protected void mergeEntity(CheckEntity entity, CreateUpdateCheckDto dto) {
        if(dto.getId() != null)
            entity.setId(dto.getId());
        if(dto.getEmployeeId() != null)
            entity.setEmployee(employeeService.getById(dto.getEmployeeId()));
        if(dto.getCustomerCardId() != null)
            entity.setCustomerCard(customerCardService.getById(dto.getCustomerCardId()));
        if(dto.getPrintDate() != null)
            entity.setPrintDate(dto.getPrintDate());
        if(dto.getSumTotal() != null)
            entity.setSumTotal(dto.getSumTotal());
        if(dto.getVat() != null)
            entity.setVat(dto.getVat());
    }
}
