package com.application.service;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.exception.PrescoringException;
import com.application.integration.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final DealService dealService;

    public List<LoanOfferDTO> createLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PrescoringException {
        log.info("loan application: " + loanApplicationRequestDTO);
        calculatePrescoring(loanApplicationRequestDTO);

        return dealService.createLoanApplication(loanApplicationRequestDTO);
    }

    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Loan Offer: " + loanOfferDTO);
        dealService.applyOffer(loanOfferDTO);
    }

    public void calculatePrescoring(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PrescoringException {

        ArrayList<String> exception = new ArrayList<>();

        if (loanApplicationRequestDTO.getAmount().compareTo(new BigDecimal("10000")) < 0) {
            exception.add("Amount cannot be less 10 000");
        }
        if (loanApplicationRequestDTO.getTerm() < 6) {
            exception.add("Term cannot be less 6");
        }
        if (notValidSize(2, 30, loanApplicationRequestDTO.getFirstName())) {
            exception.add("First name must be between 2 and 30 characters");
        }
        if (notValidSize(2, 30, loanApplicationRequestDTO.getLastName())) {
            exception.add("Last name must be between 2 and 30 characters");
        }
        if (loanApplicationRequestDTO.getMiddleName() != null &&
                notValidSize(2, 30, loanApplicationRequestDTO.getMiddleName())) {
            exception.add("Middle name must be between 2 and 30 characters");
        }
        String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.compile(regexPattern)
                .matcher(loanApplicationRequestDTO.getEmail())
                .matches()) {
            exception.add("Email address has invalid format. Does not match the pattern: " + regexPattern);
        }

        LocalDate localDate = loanApplicationRequestDTO.getBirthdate();
        LocalDate localDateNow = LocalDate.now();
        long years = localDate.until(localDateNow, ChronoUnit.YEARS);
        log.info("User years = " + years);
        long minUserAge = 18;
        if (years < minUserAge) {
            exception.add("Age less than 18 years");
        }
        if (notValidSize(4, 4, loanApplicationRequestDTO.getPassportSeries())) {
            exception.add("Passport series must consist 4 characters");
        }
        if (notValidSize(6, 6, loanApplicationRequestDTO.getPassportNumber())) {
            exception.add("Passport number must consist 6 characters");
        }

        if (!exception.isEmpty()) {
            throw new PrescoringException(exception);
        }
    }

    public boolean notValidSize(int minSize, int maxSize, String contactField) {
        return contactField.length() < minSize || contactField.length() > maxSize;
    }

}
