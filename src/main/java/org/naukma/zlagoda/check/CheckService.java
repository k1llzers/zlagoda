package org.naukma.zlagoda.check;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.check.dto.CheckResponseDto;
import org.naukma.zlagoda.check.dto.CreateUpdateCheckDto;
import org.naukma.zlagoda.customercard.CustomerCardService;
import org.naukma.zlagoda.employee.EmployeeService;
import org.naukma.zlagoda.sale.SaleService;
import org.naukma.zlagoda.storeproduct.StoreProductEntity;
import org.naukma.zlagoda.storeproduct.StoreProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckService extends BaseService<CreateUpdateCheckDto, CheckEntity, Integer> {

    private final CheckMapper mapper;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;
    private final StoreProductService storeProductService;
    private final SaleService saleService;

    public CheckService(CheckMapper mapper, EmployeeService employeeService, CustomerCardService customerCardService, StoreProductService storeProductService, SaleService saleService) {
        super(CheckEntity::new, CheckEntity.class);
        this.mapper = mapper;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;
        this.storeProductService = storeProductService;
        this.saleService = saleService;
    }

    @Override
    public Integer save(CreateUpdateCheckDto dto) {
        Integer save = super.save(dto);
        createSales(dto.getProductIdToCountMap(), save);
        return save;
    }

    @Override
    public Boolean update(CreateUpdateCheckDto dto) {
        Boolean update = super.update(dto);
        createSales(dto.getProductIdToCountMap(), dto.getId());
        return update;
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
        if (dto.getProductIdToCountMap() != null) {
            Map<StoreProductEntity, Integer> productToCount = getProductEntityToCountMap(dto.getProductIdToCountMap());
            entity.setSumTotal(countSumTotal(productToCount));
        }
        entity.setPrintDate(LocalDateTime.now());
        entity.setVat(entity.getSumTotal().multiply(BigDecimal.valueOf(0.2)));
    }

    private Map<StoreProductEntity, Integer> getProductEntityToCountMap(Map<Integer, Integer> productIdToCount) {
        return productIdToCount.entrySet().stream()
                .collect(Collectors.toMap(entry -> storeProductService.getById(entry.getKey()), Map.Entry::getValue));
    }

    private BigDecimal countSumTotal(Map<StoreProductEntity, Integer> productEntityToCount) {
        return productEntityToCount.entrySet().stream()
                .map(entry -> {
                    StoreProductEntity storeProduct = entry.getKey();
                    if (entry.getKey().getPromotion() != null)
                        storeProduct = entry.getKey().getPromotion();
                    return storeProduct.getSellingPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void createSales(Map<Integer, Integer> productIdToCount, Integer checkId) {
        if (productIdToCount == null) return;
        CheckEntity entity = getById(checkId);
        getProductEntityToCountMap(productIdToCount).forEach((key, value) -> saleService.save(key, entity, value));
    }
}
