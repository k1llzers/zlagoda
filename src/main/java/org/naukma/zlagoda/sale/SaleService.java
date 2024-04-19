package org.naukma.zlagoda.sale;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.check.CheckEntity;
import org.naukma.zlagoda.storeproduct.StoreProductEntity;
import org.naukma.zlagoda.storeproduct.StoreProductRepository;
import org.naukma.zlagoda.storeproduct.StoreProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final StoreProductRepository storeProductRepository;
    private final StoreProductService storeProductService;

    public SaleId save(StoreProductEntity storeProductEntity, CheckEntity checkEntity, Integer productNumber) {
        SaleEntity entity = new SaleEntity();
        Optional<StoreProductEntity> byPromotionId = storeProductRepository.findByPromotionId(storeProductEntity.getId());
        if (byPromotionId.isPresent()) {
            entity.setId(new SaleId(byPromotionId.get(), checkEntity));
        } else {
            entity.setId(new SaleId(storeProductEntity, checkEntity));
        }
        entity.setProductNumber(productNumber);
        entity.setSellingPrice(storeProductEntity.getSellingPrice());
        SaleId toReturn = saleRepository.save(entity);
        storeProductEntity.setProductsNumber(storeProductEntity.getProductsNumber() - productNumber);
        storeProductService.updateByEntity(storeProductEntity);
        return toReturn;
    }

    public List<SaleEntity> findAllByCheckNumber(Integer checkNumber) {
        return saleRepository.findAllByCheckNumber(checkNumber);
    }
}
