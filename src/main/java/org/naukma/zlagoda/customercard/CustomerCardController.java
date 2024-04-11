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
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<CustomerCardResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getResponseDto(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<CustomerCardResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/surname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CustomerCardResponseDto>> getAllOrderBySurname() {
        return ResponseEntity.ok(service.getAllOrderBySurname());
    }

    @GetMapping("/order-by/surname/{percent}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<CustomerCardResponseDto>> getAllCustomerWithPercentOrderBySurname(@PathVariable Integer percent) {
        return ResponseEntity.ok(service.getAllCustomerWithPercentOrderBySurname(percent));
    }

    @GetMapping("/by-surname")
    @PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public ResponseEntity<List<CustomerCardResponseDto>> getAllBySurname(@RequestParam String surname) {
        return ResponseEntity.ok(service.getAllBySurname(surname));
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateCustomerCardDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateCustomerCardDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }
}
