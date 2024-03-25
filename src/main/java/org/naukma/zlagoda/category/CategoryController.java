package org.naukma.zlagoda.category;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.category.dto.CategoryResponseDto;
import org.naukma.zlagoda.category.dto.CreateUpdateCategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCategoryResponseDto(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody CreateUpdateCategoryDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateCategoryDto body) {
        return ResponseEntity.ok(service.update(body));
    }

}
