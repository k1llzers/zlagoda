package org.naukma.zlagoda.check;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.check.dto.CheckResponseDto;
import org.naukma.zlagoda.check.dto.CreateUpdateCheckDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;

@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {
    private final CheckService service;

    @GetMapping("/{id}")
    public ResponseEntity<CheckResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCheckResponseDto(id));
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
    public ResponseEntity<Boolean> update(@RequestBody @Valid CreateUpdateCheckDto body) {
        return ResponseEntity.ok(service.update(body));
    }
}
