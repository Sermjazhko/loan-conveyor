package com.application.controller;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.exception.PrescoringException;
import com.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Application controller", description = "Operations with conducting deals")
@RestController
@Validated
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(
            summary = "Сalculation of possible loan terms.",
            description = "Data prescoring and application creation."
    )
    @PostMapping("")
    public List<LoanOfferDTO> createApplication(@Valid @RequestBody @Parameter(description = "Application for a loan")
        LoanApplicationRequestDTO loanApplicationRequestDTO) throws PrescoringException {

        log.info("Input data to the offer, Loan Application Request: " + loanApplicationRequestDTO);
        List<LoanOfferDTO> loanOfferDTOS = applicationService.createLoanApplication(loanApplicationRequestDTO);
        log.info("Output data to the offer, list Loan Offer: " + loanOfferDTOS);

        return loanOfferDTOS;
    }

    @Operation(
            summary = "Choosing one of the offers",
            description = "Updating the application statuses and accepting the offer"
    )
    @PutMapping("/offer")
    public void applyOffer(@Valid @RequestBody @Parameter(description = "loan offer") LoanOfferDTO loanOfferDTO) {
        log.info("Input data to the calculation, loan offer DTO: " + loanOfferDTO);
        applicationService.applyOffer(loanOfferDTO);
    }
}
