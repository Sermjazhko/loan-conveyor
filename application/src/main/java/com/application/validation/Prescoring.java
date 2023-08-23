package com.application.validation;

import com.application.dto.LoanApplicationRequestDTO;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
public class Prescoring {

    private static  long minUserAge = 18;

    public static boolean isValidDate(LocalDate localDate) {

        log.info("Date: " + localDate);

        if (localDate == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        if (!(localDate.toString().matches("\\d{4}-\\d{2}-\\d{2}"))) {
            throw new IllegalArgumentException("Invalid date format");
        }
        LocalDate localDateNow = LocalDate.now();
        long years = localDate.until(localDateNow, ChronoUnit.YEARS);

        log.info("User years = " + years);

        if (years < minUserAge) {
            throw new IllegalArgumentException("Age less than 18 years");
        }
        return true;
    }

    public static boolean checkLoanApplicationRequestDTO(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("Loan application request: " + loanApplicationRequestDTO);

        isValidDate(loanApplicationRequestDTO.getBirthdate());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<LoanApplicationRequestDTO>> constraintViolations =
                validator.validate(loanApplicationRequestDTO);

        if (constraintViolations.size() > 0) {
            throw new IllegalArgumentException(constraintViolations.iterator().next().getMessage());
        } else {
            return true;
        }
    }
}
