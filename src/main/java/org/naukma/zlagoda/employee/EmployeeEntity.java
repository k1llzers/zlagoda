package org.naukma.zlagoda.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.abstraction.repository.GettableById;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity implements GettableById<Integer>, UserDetails {
    private Integer id;
    @NotNull(message = "Login can't be null.")
    @NotBlank(message = "Login can't be blank.")
    @Size(max=20, message = "Login size can't be more than 20.")
    private String login;
    private String password;
    @NotNull(message = "Employee surname can't be null.")
    @NotBlank(message = "Employee surname can't be blank.")
    @Size(max=100, message = "Employee surname size can't be more than 50.")
    private String surname;
    @NotNull(message = "Employee name can't be null.")
    @NotBlank(message = "Employee name can't be blank.")
    @Size(max=100, message = "Employee name size can't be more than 50.")
    private String name;
    @Size(max=100, message = "Employee patronymic size can't be more than 50.")
    private String patronymic;
    @NotNull(message = "Employee role can't be null.")
    private Role role;
    @NotNull(message = "Salary can't be null.")
    @DecimalMin(value = "0", message = "Salary can't be less than zero.")
    private BigDecimal salary;
    @NotNull(message = "Date of birth can't be null.")
    @Past(message = "Date of birth can't be in future.")
    private LocalDate dateOfBirth;
    @NotNull(message = "Date of start can't be null.")
    private LocalDate dateOfStart;
    @NotNull(message = "Phone number can't be null.")
    @NotBlank(message = "Phone number can't be blank.")
    @Size(max=13, message = "Phone number size can't be more than 13.")
    private String phoneNumber;
    @NotNull(message = "City can't be null.")
    @NotBlank(message = "City can't be blank.")
    @Size(max=50, message = "City size can't be more than 50.")
    private String city;
    @NotNull(message = "Street can't be null.")
    @NotBlank(message = "Street can't be blank.")
    @Size(max=50, message = "Street size can't be more than 50.")
    private String street;
    @NotNull(message = "Zip code can't be null.")
    @NotBlank(message = "Zip code can't be blank.")
    @Size(max=9, message = "Zip code size can't be more than 9.")
    private String zipCode;

    public String getFullName() {
        return surname + " " + name + " " + (patronymic == null ? "" :patronymic);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
