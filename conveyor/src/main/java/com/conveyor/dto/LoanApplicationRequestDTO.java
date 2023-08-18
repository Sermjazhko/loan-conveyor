package com.conveyor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Application form for a loan")
public class LoanApplicationRequestDTO {

    @NotNull(message = "Amount cannot be null")
    @Min(value = 10000, message = "Amount cannot be less 10 000")
    private BigDecimal amount;

    @NotNull(message = "Term cannot be null")
    @Min(value = 6, message = "Term cannot be less 6")
    private Integer term;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 30, message
            = "First name must be between 2 and 30 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 30, message
            = "Last name must be between 2 and 30 characters")
    private String lastName;

    @Size(min = 2, max = 30, message
            = "Middle name must be between 2 and 30 characters")
    private String middleName;

    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    private LocalDate birthdate;

    @NotNull(message = "Passport series cannot be null")
    @Size(min = 4, max = 4, message = "Passport series must consist 4 characters")
    private String passportSeries;

    @NotNull(message = "Passport number cannot be null")
    @Size(min = 6, max = 6, message = "Passport number must consist 6 characters")
    private String passportNumber;

    @Override
    public String toString() {
        return "\nLoanApplicationRequestDTO{" +
                "amount= " + amount +
                ", term= " + term +
                ", firstName= '" + firstName + '\'' +
                ", lastName= '" + lastName + '\'' +
                ", middleName= '" + middleName + '\'' +
                ", email= '" + email + '\'' +
                ", birthdate= " + birthdate +
                ", passportSeries= '" + passportSeries + '\'' +
                ", passportNumber= '" + passportNumber + '\'' +
                '}';
    }
}
