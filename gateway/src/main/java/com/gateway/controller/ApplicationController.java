package com.gateway.controller;

import com.gateway.dto.LoanApplicationRequestDTO;
import com.gateway.dto.LoanOfferDTO;
import com.gateway.integration.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Application controller", description = "Integration of the application microservice")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    @Operation(
            summary = "Сalculation of possible loan terms.",
            description = "Data prescoring and application creation."
    )
    @PostMapping("/application")
    public List<LoanOfferDTO> createApplication(@RequestBody @Parameter(description = "Application for a loan")
                                                LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("Input data to the offer, Loan Application Request: " + loanApplicationRequestDTO);
        List<LoanOfferDTO> loanOfferDTOS = applicationService.createApplication(loanApplicationRequestDTO);
        log.info("Output data to the offer, list Loan Offer: " + loanOfferDTOS);

        return loanOfferDTOS;
    }

    @Operation(
            summary = "Choosing one of the offers",
            description = "Updating the application statuses and accepting the offer"
    )
    @PostMapping ("/application/apply")
    public void applyOffer(@RequestBody @Parameter(description = "loan offer") LoanOfferDTO loanOfferDTO) {
        log.info("Input data to the calculation, loan offer DTO: " + loanOfferDTO);
        applicationService.applyOffer(loanOfferDTO);
    }
}
