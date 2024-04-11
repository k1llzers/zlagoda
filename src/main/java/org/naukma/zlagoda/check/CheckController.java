package org.naukma.zlagoda.check;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.check.dto.CheckResponseDto;
import org.naukma.zlagoda.check.dto.CreateUpdateCheckDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {
    private final CheckService service;

    @GetMapping("/{id}")
    public ResponseEntity<CheckResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCheckResponseDto(id));
    }

    @GetMapping
    public ResponseEntity<List<CheckResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/by-employee")
    public ResponseEntity<List<CheckResponseDto>> getAllByCashierAndPrintDateBetween(@RequestParam(name = "empl", required = false) Integer employeeId,
                                                                                     @RequestParam(name = "from") LocalDateTime from,
                                                                                     @RequestParam(name = "to") LocalDateTime to) {
        return ResponseEntity.ok(service.getAllByCashierAndPrintDateBetween(employeeId, from, to));
    }

    @GetMapping("/by-employee/products-sum")
    public ResponseEntity<BigDecimal> getNumberOfProductsByCashierAndPrintDateBetween(@RequestParam(name = "empl", required = false) Integer employeeId,
                                                                                      @RequestParam(name = "from") LocalDateTime from,
                                                                                      @RequestParam(name = "to") LocalDateTime to) {
        return ResponseEntity.ok(service.getNumberOfProductsByCashierAndPrintDateBetween(employeeId, from, to));
    }

    @GetMapping("/count/product/{productId}")
    public ResponseEntity<Long> countByProductAndPrintDateBetween(@PathVariable Integer productId,
                                                                                     @RequestParam(name = "from")LocalDateTime from,
                                                                                     @RequestParam(name = "to")LocalDateTime to) {
        return ResponseEntity.ok(service.countByProductAndPrintDateBetween(productId, from, to));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateUpdateCheckDto body) {
        return ResponseEntity.ok(service.save(body));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody CreateUpdateCheckDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
