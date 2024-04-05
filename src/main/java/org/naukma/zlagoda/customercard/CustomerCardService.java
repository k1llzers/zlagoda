package org.naukma.zlagoda.customercard;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.customercard.dto.CreateUpdateCustomerCardDto;
import org.naukma.zlagoda.customercard.dto.CustomerCardResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCardService extends BaseService<CreateUpdateCustomerCardDto, CustomerCardEntity, Integer> {
    private final CustomerCardMapper mapper;
    public CustomerCardService(CustomerCardMapper mapper) {
        super(CustomerCardEntity::new, CustomerCardEntity.class);
        this.mapper = mapper;
    }

    public CustomerCardResponseDto getResponseDto(Integer id) {
        return mapper.toResponseDto(getById(id));
    }

    public List<CustomerCardResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<CustomerCardResponseDto> getAllOrderBySurname() {
        return mapper.toResponseDtoList(repository.findAllOrderByDefault());
    }

    public List<CustomerCardResponseDto> getAllCustomerWithPercentOrderBySurname(Integer percent) {
        return mapper.toResponseDtoList(((CustomerCardRepository)repository).findAllCustomerWithPercentOrderBySurname(percent));
    }

    public List<CustomerCardResponseDto> getAllBySurname(String surname) {
        return mapper.toResponseDtoList(((CustomerCardRepository)repository).findAllBySurname(surname));
    }

    @Override
    protected void mergeEntity(CustomerCardEntity entity, CreateUpdateCustomerCardDto dto) {
        if (dto.getId() != null)
            entity.setId(dto.getId());
        if (dto.getSurname() != null)
            entity.setSurname(dto.getSurname());
        if (dto.getName() != null)
            entity.setName(dto.getName());
        if (dto.getPatronymic() != null)
            entity.setPatronymic(dto.getPatronymic());
        if (dto.getPhoneNumber() != null)
            entity.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getCity() != null)
            entity.setCity(dto.getCity());
        if (dto.getStreet() != null)
            entity.setStreet(dto.getStreet());
        if (dto.getZipCode() != null)
            entity.setZipCode(dto.getZipCode());
        if (dto.getPercent() != null)
            entity.setPercent(dto.getPercent());
    }
}
