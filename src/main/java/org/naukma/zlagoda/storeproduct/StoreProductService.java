package org.naukma.zlagoda.storeproduct;

import jakarta.validation.ValidationException;
import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.naukma.zlagoda.product.ProductService;
import org.naukma.zlagoda.storeproduct.dto.CreateUpdateStoreProductDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductShortResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StoreProductService extends BaseService<CreateUpdateStoreProductDto, StoreProductEntity, Integer> {
    private final StoreProductMapper mapper;
    private final ProductService productService;

    public StoreProductService(StoreProductMapper mapper, ProductService productService) {
        super(StoreProductEntity::new, "store product");
        this.mapper = mapper;
        this.productService = productService;
    }

    public StoreProductShortResponseDto getResponseDto(Integer id) {
        return mapper.toShortResponseDto(getById(id));
    }

    public List<StoreProductResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<StoreProductResponseDto> getAllOrderByCount() {
        return mapper.toResponseDtoList(repository.findAllOrderByDefault());
    }

    public List<StoreProductResponseDto> getAllPromotionalOrderByNumberName(boolean promotional) {
        return mapper.toResponseDtoList(((StoreProductRepository)repository).findAllPromotionalOrderByNumberName(promotional));
    }

    public List<StoreProductResponseDto> getAllOrderByName() {
        return mapper.toResponseDtoList(((StoreProductRepository)repository).findAllOrderByName());
    }

    public List<StoreProductResponseDto> getAllByProductNameLike(String productName) {
        return mapper.toResponseDtoList(((StoreProductRepository)repository).findAllByProductNameLike(productName));
    }

    public Boolean makePromotional(Integer id) {
        StoreProductEntity promotional = getById(id);
        if (promotional.getPromotion() != null) {
            throw new ValidationException("Can`t add more than one promotion");
        }
        promotional.setPromotional(true);
        promotional.setSellingPrice(promotional.getSellingPrice().multiply(BigDecimal.valueOf(0.8)));
        promotional.setId(null);
        promotional.setId(repository.save(promotional));
        StoreProductEntity setPromotion = getById(id);
        setPromotion.setPromotion(promotional);
        setPromotion.setProductsNumber(0);
        return repository.update(setPromotion);
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
