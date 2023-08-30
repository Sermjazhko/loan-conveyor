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

    private static final long MIN_USER_AGE = 18;

    private static final String REGEX_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

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

    private void calculatePrescoring(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PrescoringException {

        List<String> scoringRefuseCauses = new ArrayList<>();

        if (loanApplicationRequestDTO.getAmount().compareTo(new BigDecimal("10000")) < 0) {
            scoringRefuseCauses.add("Amount cannot be less 10 000");
        }
        if (loanApplicationRequestDTO.getTerm() < 6) {
            scoringRefuseCauses.add("Term cannot be less 6");
        }
        if (notValidSize(2, 30, loanApplicationRequestDTO.getFirstName())) {
            scoringRefuseCauses.add("First name must be between 2 and 30 characters");
        }
        if (notValidSize(2, 30, loanApplicationRequestDTO.getLastName())) {
            scoringRefuseCauses.add("Last name must be between 2 and 30 characters");
        }
        if (loanApplicationRequestDTO.getMiddleName() != null &&
                notValidSize(2, 30, loanApplicationRequestDTO.getMiddleName())) {
            scoringRefuseCauses.add("Middle name must be between 2 and 30 characters");
        }
        if (!Pattern.compile(REGEX_PATTERN)
                .matcher(loanApplicationRequestDTO.getEmail())
                .matches()) {
            scoringRefuseCauses.add("Email address has invalid format. Does not match the pattern: " + REGEX_PATTERN);
        }

        LocalDate birthdate = loanApplicationRequestDTO.getBirthdate();
        LocalDate localDateNow = LocalDate.now();
        long years = birthdate.until(localDateNow, ChronoUnit.YEARS);
        log.info("User years = " + years);
        if (years < MIN_USER_AGE) {
            scoringRefuseCauses.add("Age less than 18 years");
        }
        if (notValidSize(4, 4, loanApplicationRequestDTO.getPassportSeries())) {
            scoringRefuseCauses.add("Passport series must consist 4 characters");
        }
        if (notValidSize(6, 6, loanApplicationRequestDTO.getPassportNumber())) {
            scoringRefuseCauses.add("Passport number must consist 6 characters");
        }

        if (!scoringRefuseCauses.isEmpty()) {
            throw new PrescoringException(scoringRefuseCauses);
        }
    }

    private boolean notValidSize(int minSize, int maxSize, String text) {
        return text.length() < minSize || text.length() > maxSize;
    }

}
