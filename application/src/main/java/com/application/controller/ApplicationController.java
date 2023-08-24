package com.application.controller;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Application controller", description = "Operations with conducting deals")
@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Operation(
            summary = "Сalculation of possible loan terms.",
            description = "Data prescoring and application creation."
    )
    @PostMapping("")
    public List<LoanOfferDTO> getListOfOffer(@RequestBody
                                           @Parameter(description = "Application for a loan")
                                                   LoanApplicationRequestDTO loanApplicationRequestDTO) {
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
    public void setOffer(@RequestBody @Parameter(description = "loan offer")
                                          LoanOfferDTO loanOfferDTO) {
        log.info("Input data to the calculation, loan offer DTO: " + loanOfferDTO);
        applicationService.applyOffer(loanOfferDTO);
    }
}
