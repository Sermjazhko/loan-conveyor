package com.conveyor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Application form for a loan")
public class LoanApplicationRequestDTO {

    @NotNull
    @Min(10000)
    private BigDecimal amount;

    @NotNull
    @Min(6)
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

    @NotNull
    @Size(min = 4, max = 4)
    private String passportSeries;

    @NotNull
    @Size(min = 6, max = 6)
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
