package org.naukma.zlagoda.product;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.product.dto.CreateUpdateProductDto;
import org.naukma.zlagoda.product.dto.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getProductResponseDto(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody CreateUpdateProductDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateProductDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
