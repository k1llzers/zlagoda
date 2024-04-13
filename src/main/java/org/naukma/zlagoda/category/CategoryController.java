package org.naukma.zlagoda.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.category.dto.CategoryResponseDto;
import org.naukma.zlagoda.category.dto.CreateUpdateCategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCategoryResponseDto(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/name")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<CategoryResponseDto>> getAllOrderByName() {
        return ResponseEntity.ok(service.getAllOrderByName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateCategoryDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> update(@RequestBody @Valid CreateUpdateCategoryDto body) {
        return ResponseEntity.ok(service.update(body));
    }

}
