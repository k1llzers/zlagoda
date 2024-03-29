package org.naukma.zlagoda.sale;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.check.CheckEntity;
import org.naukma.zlagoda.storeproduct.StoreProductEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;

    public SaleId save(StoreProductEntity storeProductEntity, CheckEntity checkEntity, Integer productNumber) {
        SaleEntity entity = new SaleEntity();
        entity.setId(new SaleId(storeProductEntity, checkEntity));
        entity.setProductNumber(productNumber);
        entity.setSellingPrice(storeProductEntity.getSellingPrice().multiply(BigDecimal.valueOf(productNumber)));
        return saleRepository.save(entity);
    }
}
