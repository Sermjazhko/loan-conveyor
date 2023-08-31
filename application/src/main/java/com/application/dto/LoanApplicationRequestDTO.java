package com.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Application form for a loan")
public class LoanApplicationRequestDTO {

    @Schema(description = "Amount of credit", example = "300000.00")
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    @Schema(description = "Loan term", example = "12")
    @NotNull(message = "Term cannot be null")
    private Integer term;

    @Schema(description = "First name", example = "Alex")
    @NotNull(message = "First name cannot be null")
    private String firstName;

    @Schema(description = "Last name", example = "Desh")
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @Schema(description = "Middle name", example = "Tom")
    private String middleName;

    @Schema(description = "Email address", example = "example@mail.ru")
    @NotNull
    private String email;

    @Schema(description = "Birthdate", example = "2000-01-01")
    @NotNull
    private LocalDate birthdate;

    @Schema(description = "Passport series", example = "1234")
    @NotNull(message = "Passport series cannot be null")
    private String passportSeries;

    @Schema(description = "Passport number", example = "123456")
    @NotNull(message = "Passport number cannot be null")
    private String passportNumber;
}
