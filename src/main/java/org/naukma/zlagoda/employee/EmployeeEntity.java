package org.naukma.zlagoda.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity implements GettableById<Integer> {
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
