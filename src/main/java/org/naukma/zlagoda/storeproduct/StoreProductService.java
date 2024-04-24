package org.naukma.zlagoda.storeproduct;

import org.naukma.zlagoda.abstraction.service.BaseService;
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
        return mapper.toResponseDtoList(((StoreProductRepository)repository).findAllWhereCountNotZero());
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

    public List<StoreProductResponseDto> getAllByUpcLike(Integer upc) {
        return mapper.toResponseDtoList(((StoreProductRepository)repository).findAllByUpcLike(upc));
    }

    public List<StoreProductResponseDto> getMostPopularProductByCategory(Integer categoryId) {
        return mapper.toResponseDtoList(((StoreProductRepository)repository).findMostPopularProductByCategory(categoryId));
    }

    public Boolean makePromotional(Integer id) {
        StoreProductEntity product = getById(id);
        if (product.getPromotion() == null) {
            product.setPromotional(true);
            product.setSellingPrice(product.getSellingPrice().multiply(BigDecimal.valueOf(0.8)));
            product.setId(null);
            product.setId(repository.save(product));
            StoreProductEntity setPromotion = getById(id);
            setPromotion.setPromotion(product);
            setPromotion.setProductsNumber(0);
            repository.update(setPromotion);
        } else {
            StoreProductEntity promotion = product.getPromotion();
            promotion.setProductsNumber(promotion.getProductsNumber() + product.getProductsNumber());
            promotion.setSellingPrice(product.getSellingPrice().multiply(BigDecimal.valueOf(0.8)));
            repository.update(promotion);
            product.setProductsNumber(0);
            repository.update(product);
        }
        return true;
    }

    public Boolean unpromotion(Integer id) {
        StoreProductEntity product = getById(id);
        product.setProductsNumber(0);
        return repository.update(product);
    }

    @Override
    public Boolean delete(Integer id) {
        StoreProductEntity byId = getById(id);
        repository.delete(id);
        if (byId.getPromotion() != null) {
            repository.delete(byId.getPromotion().getId());
        }
        return true;
    }

    public Boolean updateByEntity(StoreProductEntity entity) {
        if (entity.getProductsNumber() == 0)
            return repository.delete(entity.getId());
        else
            return repository.update(entity);
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
