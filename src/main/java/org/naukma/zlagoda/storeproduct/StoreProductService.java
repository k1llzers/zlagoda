package org.naukma.zlagoda.storeproduct;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.product.ProductService;
import org.naukma.zlagoda.storeproduct.dto.CreateUpdateStoreProductDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreProductService extends BaseService<CreateUpdateStoreProductDto, StoreProductEntity, Integer> {
    private final StoreProductMapper mapper;
    private final ProductService productService;

    public StoreProductService(StoreProductMapper mapper, ProductService productService) {
        super(StoreProductEntity::new, StoreProductEntity.class);
        this.mapper = mapper;
        this.productService = productService;
    }

    public StoreProductResponseDto getResponseDto(Integer id) {
        return mapper.toResponseDto(getById(id));
    }

    public List<StoreProductResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<StoreProductResponseDto> getAllOrderByCount() {
        return mapper.toResponseDtoList(repository.findAllOrderBy());
    }

    @Override
    protected void mergeEntity(StoreProductEntity entity, CreateUpdateStoreProductDto dto) {
        if (dto.getId() != null)
            entity.setId(dto.getId());
        if (dto.getPromotionId() != null)
            entity.setPromotion(getById(dto.getPromotionId()));
        if (dto.getProductId() != null)
            entity.setProduct(productService.getById(dto.getProductId()));
        if (dto.getSellingPrice() != null)
            entity.setSellingPrice(dto.getSellingPrice());
        if (dto.getProductsNumber() != null)
            entity.setProductsNumber(dto.getProductsNumber());
        if (dto.getPromotional() != null)
            entity.setPromotional(dto.getPromotional());
    }
}
