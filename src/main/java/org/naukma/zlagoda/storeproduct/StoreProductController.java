package org.naukma.zlagoda.storeproduct;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.storeproduct.dto.CreateUpdateStoreProductDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store-product")
@RequiredArgsConstructor
public class StoreProductController {
    private final StoreProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<StoreProductResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getResponseDto(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateStoreProductDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody @Valid CreateUpdateStoreProductDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
