package org.naukma.zlagoda.check;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.check.dto.CheckResponseDto;
import org.naukma.zlagoda.check.dto.CreateUpdateCheckDto;
import org.naukma.zlagoda.customercard.CustomerCardService;
import org.naukma.zlagoda.employee.EmployeeEntity;
import org.naukma.zlagoda.employee.EmployeeService;
import org.naukma.zlagoda.employee.Role;
import org.naukma.zlagoda.sale.SaleService;
import org.naukma.zlagoda.storeproduct.StoreProductEntity;
import org.naukma.zlagoda.storeproduct.StoreProductService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckService extends BaseService<CreateUpdateCheckDto, CheckEntity, Integer> {

    private final CheckMapper mapper;
    private final CustomerCardService customerCardService;
    private final StoreProductService storeProductService;
    private final SaleService saleService;

    public CheckService(CheckMapper mapper, CustomerCardService customerCardService, StoreProductService storeProductService, SaleService saleService) {
        super(CheckEntity::new, "check");
        this.mapper = mapper;
        this.customerCardService = customerCardService;
        this.storeProductService = storeProductService;
        this.saleService = saleService;
    }

    @Override
    public Integer save(CreateUpdateCheckDto dto) {
        Integer id = super.save(dto);
        createSales(dto.getProductIdToCountMap(), id);
        return id;
    }

    @Override
    public Boolean update(CreateUpdateCheckDto dto) {
        Boolean update = super.update(dto);
        createSales(dto.getProductIdToCountMap(), dto.getId());
        return update;
    }

    public CheckResponseDto getCheckResponseDto(Integer id) {
        CheckEntity result = getById(id);
        result.setSales(saleService.findAllByCheckNumber(result.getId()));
        return mapper.toResponseDto(result);
    }

    public List<CheckResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<CheckResponseDto> getAllByIdLike(Integer id) {
        return mapper.toResponseDtoList(((CheckRepository)repository).findAllByIdLike(id));
    }

    public List<CheckResponseDto> getAllByCashierAndPrintDateBetween(Integer id, LocalDateTime from, LocalDateTime to) {
        EmployeeEntity user = (EmployeeEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole() == Role.CASHIER)
            id = user.getId();
        List<CheckEntity> result = ((CheckRepository) repository).findAllByCashierAndPrintDateBetween(id, from, to).stream()
                .peek(check -> check.setSales(saleService.findAllByCheckNumber(check.getId())))
                .toList();
        return mapper.toResponseDtoList(result);
    }

    public BigDecimal getNumberOfProductsByCashierAndPrintDateBetween(Integer id, LocalDateTime from, LocalDateTime to) {
        return ((CheckRepository)repository).findNumberOfProductsByCashierAndPrintDateBetween(id, from, to);
    }

    public Long countByProductAndPrintDateBetween(Integer productId, LocalDateTime from, LocalDateTime to) {
        return ((CheckRepository) repository).countByProductAndPrintDateBetween(productId, from, to);
    }

    @Override
    protected void mergeEntity(CheckEntity entity, CreateUpdateCheckDto dto) {
        if(dto.getId() != null)
            entity.setId(dto.getId());
        if(dto.getCustomerCardId() != null) {
            entity.setCustomerCard(customerCardService.getById(dto.getCustomerCardId()));
        }
        if (dto.getCustomerCardId() != null) {
            entity.setSumTotal(countSumTotal(dto).multiply(BigDecimal.valueOf((100.0 - entity.getCustomerCard().getPercent()) / 100)));
        } else {
            entity.setSumTotal(countSumTotal(dto));
        }
        entity.setEmployee((EmployeeEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        entity.setPrintDate(LocalDateTime.now());
        entity.setVat(entity.getSumTotal().multiply(BigDecimal.valueOf(0.2)));
    }

    private Map<StoreProductEntity, Integer> getProductEntityToCountMap(Map<Integer, Integer> productIdToCount) {
        return productIdToCount.entrySet().stream()
                .collect(Collectors.toMap(entry -> storeProductService.getById(entry.getKey()), Map.Entry::getValue));
    }

    private BigDecimal countSumTotal(CreateUpdateCheckDto dto) {
        return getProductEntityToCountMap(dto.getProductIdToCountMap()).entrySet().stream()
                .map(entry -> {
                    StoreProductEntity storeProduct = entry.getKey();
                    return storeProduct.getSellingPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void createSales(Map<Integer, Integer> productIdToCount, Integer checkId) {
        if (productIdToCount == null) return;
        CheckEntity entity = getById(checkId);
        getProductEntityToCountMap(productIdToCount).forEach((key, value) -> saleService.save(key, entity, value));
    }
}
