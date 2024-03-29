package org.naukma.zlagoda.customercard.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.naukma.zlagoda.abstraction.repository.GettableById;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateCustomerCardDto implements GettableById<Integer> {
    private Integer id;
    @NotNull(message = "Customer surname can't be null.")
    @NotBlank(message = "Customer surname can't be blank.")
    @Size(max=50, message = "Customer surname size can't be more than 50.")
    private String surname;
    @NotNull(message = "Customer name can't be null.")
    @NotBlank(message = "Customer name can't be blank.")
    @Size(max=50, message = "Customer name size can't be more than 50.")
    private String name;
    @NotBlank(message = "Customer patronymic can't be blank.")
    @Size(max=50, message = "Customer patronymic size can't be more than 50.")
    private String patronymic;
    @NotNull(message = "Customer phone number can't be null.")
    @NotBlank(message = "Customer phone number can't be blank.")
    @Size(max=13, message = "Customer phone number size can't be more than 13.")
    private String phoneNumber;
    @NotBlank(message = "City can't be blank.")
    @Size(max=50, message = "City size can't be more than 50.")
    private String city;
    @NotBlank(message = "Street can't be blank.")
    @Size(max=50, message = "Street size can't be more than 50.")
    private String street;
    @NotBlank(message = "Zip code can't be blank.")
    @Size(max=9, message = "Zip code size can't be more than 9.")
    private String zipCode;
    @NotNull(message = "Percent can't be null.")
    @Min(value = 0, message = "Percent can't be less than zero.")
    @Max(value = 100, message = "Percent can't be more than 100.")
    private Integer percent;
}
