package org.naukma.zlagoda.category;

import org.naukma.zlagoda.abstraction.service.BaseService;
import org.naukma.zlagoda.category.dto.CategoryResponseDto;
import org.naukma.zlagoda.category.dto.CreateUpdateCategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends BaseService<CreateUpdateCategoryDto, CategoryEntity, Integer> {

    private final CategoryMapper mapper;

    public CategoryService(CategoryMapper mapper){
        super(CategoryEntity::new, "category");
        this.mapper = mapper;
    }

    public List<CategoryResponseDto> getAll() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    public List<CategoryResponseDto> getAllOrderByName() {
        return mapper.toResponseDtoList(repository.findAllOrderByDefault());
    }

    public CategoryResponseDto getCategoryResponseDto(Integer id){
        return mapper.toResponseDto(getById(id));
    }

    @Override
    protected void mergeEntity(CategoryEntity entity, CreateUpdateCategoryDto dto) {
        if(dto.getId() != null)
            entity.setId(dto.getId());
        if(dto.getName() != null)
            entity.setName(dto.getName());
    }
}
