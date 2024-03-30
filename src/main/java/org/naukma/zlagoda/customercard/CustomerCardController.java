package org.naukma.zlagoda.customercard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.customercard.dto.CreateUpdateCustomerCardDto;
import org.naukma.zlagoda.customercard.dto.CustomerCardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer-card")
@RequiredArgsConstructor
public class CustomerCardController {
    private final CustomerCardService service;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerCardResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getResponseDto(id));
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
    public ResponseEntity<Boolean> update(@RequestBody @Valid CreateUpdateCustomerCardDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }
}
