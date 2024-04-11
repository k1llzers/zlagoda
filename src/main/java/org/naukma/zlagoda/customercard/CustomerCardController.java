package org.naukma.zlagoda.customercard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.customercard.dto.CreateUpdateCustomerCardDto;
import org.naukma.zlagoda.customercard.dto.CustomerCardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-card")
@RequiredArgsConstructor
public class CustomerCardController {
    private final CustomerCardService service;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<CustomerCardResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getResponseDto(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerCardResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/surname")
    public ResponseEntity<List<CustomerCardResponseDto>> getAllOrderBySurname() {
        return ResponseEntity.ok(service.getAllOrderBySurname());
    }

    @GetMapping("/order-by/surname/{percent}")
    public ResponseEntity<List<CustomerCardResponseDto>> getAllCustomerWithPercentOrderBySurname(@PathVariable Integer percent) {
        return ResponseEntity.ok(service.getAllCustomerWithPercentOrderBySurname(percent));
    }

    @GetMapping("/by-surname")
    public ResponseEntity<List<CustomerCardResponseDto>> getAllBySurname(@RequestParam String surname) {
        return ResponseEntity.ok(service.getAllBySurname(surname));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateCustomerCardDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateCustomerCardDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }
}
