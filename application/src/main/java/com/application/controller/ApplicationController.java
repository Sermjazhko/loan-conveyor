package com.application.controller;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.validation.DataValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Tag(name="Application controller", description="Implements the business logic of the credit pipeline")
@RestController
@RequestMapping("/application")
public class ApplicationController {

    private static Logger log = Logger.getLogger(ApplicationController.class.getName());


    @Operation(
            summary = "Prescoring + request for calculation of possible loan terms.",
            description = "The API sends a Loan Application Request TO" +
                    "Based on LoanApplicationRequestDTO, prescoring occurs." +
                    "A POST request is sent to /deal/application in MS deal via FeignClient." +
                    "The response to the API is a list of 4 LoanOfferDTO from worst to best."
    )
    @PostMapping("")
    public ResponseEntity<List<LoanOfferDTO>> getPostOffer(@RequestBody
                                                               @Parameter(description = "Application for a loan")
                                                               LoanApplicationRequestDTO loanApplicationRequestDTO) {
        try {
            log.info("Input data to the offer, Loan Application Request: " + loanApplicationRequestDTO);

            DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
            String resourceUrl = "http://localhost:9080/deal/application";
            RestTemplate restTemplate = new RestTemplate();

            log.info("Start POST request!");
            HttpEntity<LoanApplicationRequestDTO> request =
                    new HttpEntity<LoanApplicationRequestDTO>(loanApplicationRequestDTO);

            ResponseEntity<List<LoanOfferDTO>> rateResponse =
                    restTemplate.exchange(resourceUrl,
                            HttpMethod.POST, request, new ParameterizedTypeReference<List<LoanOfferDTO>>() {
                            });
            log.info("End POST request!");

            List<LoanOfferDTO> loanOfferDTOS = rateResponse.getBody();

            log.info("Output data to the offer, list Loan Offer: " + loanOfferDTOS);

            return new ResponseEntity<>(loanOfferDTOS, HttpStatus.CREATED);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Choosing one of the offers",
            description = "LoanOfferDTO comes via API" +
                    "A PUT request is sent to /deal/offer in MS deal via RestTemplate."
    )
    @PutMapping("/offer")
    public void getPostCalculation(@RequestBody @Parameter(description = "loan offer")
                                                            LoanOfferDTO loanOfferDTO) {

        try {
            log.info("Input data to the calculation, loan offer DTO: " + loanOfferDTO);

            String resourceUrl = "http://localhost:9080/deal/offer";
            RestTemplate restTemplate = new RestTemplate();

            log.info("Start PUT request!");
            restTemplate.put(resourceUrl, loanOfferDTO);
            log.info("End PUT request!");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }
    }
}
