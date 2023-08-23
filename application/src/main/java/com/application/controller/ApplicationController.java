package com.application.controller;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.service.DealResponseService;
import com.application.validation.Prescoring;
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

    private final DealResponseService dealResponseService;

    public ApplicationController(DealResponseService dealResponseService) {
        this.dealResponseService = dealResponseService;
    }

    @Operation(
            summary = "Prescoring + request for calculation of possible loan terms.",
            description = "The API sends a LoanApplicationRequestDTO\n" +
                    "The response to the API is a list of 4 LoanOfferDTO from worst to best."
    )
    @PostMapping("")
    public List<LoanOfferDTO> getPostOffer(@RequestBody
                                           @Parameter(description = "Application for a loan")
                                                   LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Input data to the offer, Loan Application Request: " + loanApplicationRequestDTO);

        Prescoring.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
        List<LoanOfferDTO> loanOfferDTOS = dealResponseService.getResultPostRequestOffer(loanApplicationRequestDTO);

        log.info("Output data to the offer, list Loan Offer: " + loanOfferDTOS);
        return loanOfferDTOS;
    }

    @Operation(
            summary = "Choosing one of the offers",
            description = "LoanOfferDTO comes via API. A PUT-request is sent to /deal/offer in MS deal via RestTemplate."
    )
    @PutMapping("/offer")
    public void getPutCalculation(@RequestBody @Parameter(description = "loan offer")
                                          LoanOfferDTO loanOfferDTO) {
        log.info("Input data to the calculation, loan offer DTO: " + loanOfferDTO);
        dealResponseService.getResultPutRequestCalculation(loanOfferDTO);
    }
}
