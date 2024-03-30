package org.naukma.zlagoda.employee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.employee.dto.CreateUpdateEmployeeDto;
import org.naukma.zlagoda.employee.dto.EmployeeResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getEmployeeResponseDto(id));
    }
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateEmployeeDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody @Valid CreateUpdateEmployeeDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
