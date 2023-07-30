package com.conveyor.controller;

import com.conveyor.dto.CreditDTO;
import com.conveyor.dto.LoanApplicationRequestDTO;
import com.conveyor.dto.LoanOfferDTO;
import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.service.ConveyorService;
import com.conveyor.service.ConveyorServiceImpl;
import com.conveyor.validation.DataValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/conveyor")
public class ConveyorController {

    private static Logger log = Logger.getLogger(ConveyorController.class.getName());

    private final ConveyorService conveyorService;

    public ConveyorController(ConveyorService conveyorService) {

        this.conveyorService = conveyorService;
    }

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> getPostOffer(@RequestBody LoanApplicationRequestDTO
                                                                       loanApplicationRequestDTO) {
        try {
            log.info("Input data to the offer, Loan Application Request: " + loanApplicationRequestDTO);

            DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
            List<LoanOfferDTO> loanOfferDTOS;
            loanOfferDTOS = conveyorService.getOffers(loanApplicationRequestDTO);

            log.info("Output data to the offer, list Loan Offer: " + loanOfferDTOS);
            return new ResponseEntity<>(loanOfferDTOS, HttpStatus.CREATED);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //так как был уже прескоринг и тут указан только скоринг,
    // считаем, что имена и прочее верно передаются
    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> getPostCalculation(@RequestBody ScoringDataDTO scoringDataDTO) {

        try {
            log.info("Input data to the calculation, Scoring Data: " + scoringDataDTO);

            CreditDTO creditDTO;
            creditDTO = conveyorService.getCalculation(scoringDataDTO);

            log.info("Output data to the calculation, Credit: " + creditDTO);
            return new ResponseEntity<>(creditDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
