package org.naukma.zlagoda.employee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.employee.dto.CreateUpdateEmployeeDto;
import org.naukma.zlagoda.employee.dto.EmployeePhoneNumberAddressDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getEmployeeResponseDto(id));
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/order-by/surname")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> getAllOrderBySurname() {
        return ResponseEntity.ok(service.getAllOrderBySurname());
    }

    @GetMapping("/cashier/order-by/surname")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> getAllCashiersOrderBySurname() {
        return ResponseEntity.ok(service.getAllCashiersOrderBySurname());
    }

    @GetMapping("/address/phone")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<EmployeeResponseDto>> getAllAddressAndPhoneBySurname(@RequestParam String surname) {
        return ResponseEntity.ok(service.getPhoneNumberAndAddressBySurname(surname));
    }

    @GetMapping("/myself")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeResponseDto> getMyselfInfo() {
        return ResponseEntity.ok(service.getMyselfInfo());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateEmployeeDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateEmployeeDto body) {
        return ResponseEntity.ok(service.update(body));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> changePassword(@RequestBody String password) {
        return ResponseEntity.ok(service.changePassword(password));
    }
}
