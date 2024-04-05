package org.naukma.zlagoda.storeproduct;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.storeproduct.dto.CreateUpdateStoreProductDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductShortResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store-product")
@RequiredArgsConstructor
public class StoreProductController {
    private final StoreProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<StoreProductShortResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getResponseDto(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreProductResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/count")
    public ResponseEntity<List<StoreProductResponseDto>> getAllOrderByCount() {
        return ResponseEntity.ok(service.getAllOrderByCount());
    }

    @GetMapping("/order-by/count/name")
    public ResponseEntity<List<StoreProductShortResponseDto>> getAllPromotionalOrderByNumberName(@RequestParam boolean promotional) {
        return ResponseEntity.ok(service.getAllPromotionalOrderByNumberName(promotional));
    }

    @GetMapping("/order-by/name")
    public ResponseEntity<List<StoreProductShortResponseDto>> getAllOrderByName() {
        return ResponseEntity.ok(service.getAllOrderByName());
    }

    @GetMapping("/by-product")
    public ResponseEntity<List<StoreProductResponseDto>> getAllByProductName(@RequestParam String productName) {
        return ResponseEntity.ok(service.getAllByProductNameLike(productName));
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
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateStoreProductDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
