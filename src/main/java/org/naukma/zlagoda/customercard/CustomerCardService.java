package org.naukma.zlagoda.customercard;

import jakarta.validation.ValidationException;
import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.customercard.dto.CreateUpdateCustomerCardDto;
import org.naukma.zlagoda.customercard.dto.CustomerCardResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCardService extends BaseService<CreateUpdateCustomerCardDto, CustomerCardEntity, Integer> {
    private final CustomerCardMapper mapper;
    public CustomerCardService(CustomerCardMapper mapper) {
        super(CustomerCardEntity::new, "customer card");
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
        if (dto.getPhoneNumber() != null)
            entity.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPercent() != null)
            entity.setPercent(dto.getPercent());
        entity.setStreet(dto.getStreet());
        entity.setZipCode(dto.getZipCode());
        entity.setCity(dto.getCity());
        entity.setPatronymic(dto.getPatronymic());
        if (dto.getPatronymic() != null && dto.getPatronymic().isBlank())
            throw new ValidationException("Customer patronymic can't be blank");
        if (dto.getZipCode() != null && dto.getZipCode().isBlank())
            throw new ValidationException("Customer zip code can't be blank");
        if (dto.getCity() != null && dto.getCity().isBlank())
            throw new ValidationException("Customer city code can't be blank");
        if (dto.getStreet() != null && dto.getStreet().isBlank())
            throw new ValidationException("Customer street code can't be blank");
    }
}
