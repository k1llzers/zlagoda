package org.naukma.zlagoda.product;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.category.CategoryService;
import org.naukma.zlagoda.product.dto.CreateUpdateProductDto;
import org.naukma.zlagoda.product.dto.ProductResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService extends BaseService<CreateUpdateProductDto, ProductEntity, Integer> {
    private final ProductMapper mapper;
    private final CategoryService categoryService;
    public ProductService(ProductMapper mapper, CategoryService categoryService) {
        super(ProductEntity::new, "product");
        this.mapper = mapper;
        this.categoryService = categoryService;
    }

    public ProductResponseDto getProductResponseDto(Integer id) {
        return mapper.toResponseDto(getById(id));
    }

    public List<ProductResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<ProductResponseDto> getAllWithoutStoreProduct() {
        return mapper.toResponseDtoList(((ProductRepository)repository).findAllWithoutStoreProduct());
    }

    public List<ProductResponseDto> getAllOrderByName() {
        return mapper.toResponseDtoList(repository.findAllOrderByDefault());
    }
    public List<ProductResponseDto> getAllProductsFromCategoryOrderByName(Integer id) {
        return mapper.toResponseDtoList(((ProductRepository)repository).findAllProductsFromCategoryOrderByName(id));
    }

    public List<ProductResponseDto> getAllByNameLike(String name) {
        return mapper.toResponseDtoList(((ProductRepository)repository).findAllProductsNameLike(name));
    }

    public List<ProductResponseDto> getMostPopularProductByCategory(Integer categoryId) {
        return mapper.toResponseDtoList(((ProductRepository)repository).findMostPopularProductByCategory(categoryId));
    }


    @Override
    protected void mergeEntity(ProductEntity entity, CreateUpdateProductDto dto) {
        if(dto.getId() != null)
            entity.setId(dto.getId());
        if(dto.getCategoryId() != null)
            entity.setCategory(categoryService.getById(dto.getCategoryId()));
        if(dto.getName() != null)
            entity.setName(dto.getName());
        if(dto.getCharacteristics() != null)
            entity.setCharacteristics(dto.getCharacteristics());
    }
}
