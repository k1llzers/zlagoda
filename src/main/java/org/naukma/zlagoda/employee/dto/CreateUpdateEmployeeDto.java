package org.naukma.zlagoda.employee.dto;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.naukma.zlagoda.employee.Role;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateEmployeeDto implements GettableById<Integer> {
    private Integer id;
    private String login;
    private String password;
    private String surname;
    private String name;
    private String patronymic;
    private Role role;
    private BigDecimal salary;
    private LocalDate dateOfBirth;
    private LocalDate dateOfStart;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
}
