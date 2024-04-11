package org.naukma.zlagoda.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.product.dto.CreateUpdateProductDto;
import org.naukma.zlagoda.product.dto.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getProductResponseDto(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/name")
    public ResponseEntity<List<ProductResponseDto>> getAllOrderByName() {
        return ResponseEntity.ok(service.getAllOrderByName());
    }

    @GetMapping("/order-by/name/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsFromCategoryOrderByName(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(service.getAllProductsFromCategoryOrderByName(categoryId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateProductDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateProductDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
