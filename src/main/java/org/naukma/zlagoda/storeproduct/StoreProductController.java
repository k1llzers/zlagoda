package org.naukma.zlagoda.storeproduct;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.storeproduct.dto.CreateUpdateStoreProductDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductResponseDto;
import org.naukma.zlagoda.storeproduct.dto.StoreProductShortResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store-product")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class StoreProductController {
    private final StoreProductService service;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StoreProductShortResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getResponseDto(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<StoreProductResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/count")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<StoreProductResponseDto>> getAllOrderByCount() {
        return ResponseEntity.ok(service.getAllOrderByCount());
    }

    @GetMapping("/order-by/count/name")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<StoreProductResponseDto>> getAllPromotionalOrderByNumberName(@RequestParam boolean promotional) {
        return ResponseEntity.ok(service.getAllPromotionalOrderByNumberName(promotional));
    }

    @GetMapping("/order-by/name")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public ResponseEntity<List<StoreProductResponseDto>> getAllOrderByName() {
        return ResponseEntity.ok(service.getAllOrderByName());
    }

    @GetMapping("/by-product")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public ResponseEntity<List<StoreProductResponseDto>> getAllByProductName(@RequestParam String productName) {
        return ResponseEntity.ok(service.getAllByProductNameLike(productName));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateStoreProductDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateStoreProductDto body) {
        return ResponseEntity.ok(service.update(body));
    }

    @PutMapping("/promotion/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> makePromotional(@PathVariable Integer id) {
        return ResponseEntity.ok(service.makePromotional(id));
    }
}
