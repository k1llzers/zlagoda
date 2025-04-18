package org.naukma.zlagoda.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.employee.Role;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Integer id;
    private String surname;
    private String name;
    private String patronymic;
    private BigDecimal salary;
    private Role role;
    private LocalDate dateOfBirth;
    private LocalDate dateOfStart;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
}
