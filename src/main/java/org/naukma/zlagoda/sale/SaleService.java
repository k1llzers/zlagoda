package org.naukma.zlagoda.sale;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.check.CheckEntity;
import org.naukma.zlagoda.storeproduct.StoreProductEntity;
import org.naukma.zlagoda.storeproduct.StoreProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final StoreProductRepository storeProductRepository;

    public SaleId save(StoreProductEntity storeProductEntity, CheckEntity checkEntity, Integer productNumber) {
        SaleEntity entity = new SaleEntity();
        entity.setId(new SaleId(storeProductEntity, checkEntity));
        entity.setProductNumber(productNumber);
        entity.setSellingPrice(storeProductEntity.getSellingPrice().multiply(BigDecimal.valueOf(productNumber)));
        SaleId toReturn = saleRepository.save(entity);
        storeProductEntity.setProductsNumber(storeProductEntity.getProductsNumber() - productNumber);
        storeProductRepository.update(storeProductEntity);
        return toReturn;
    }
}
