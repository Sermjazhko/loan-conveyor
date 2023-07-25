package com.conveyor.controller;

import com.conveyor.dto.CreditDTO;
import com.conveyor.dto.LoanApplicationRequestDTO;
import com.conveyor.dto.LoanOfferDTO;
import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.service.ConveyorService;
import com.conveyor.validation.DataValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
public class ConveyorController {

    private final ConveyorService conveyorService;

    public ConveyorController(ConveyorService conveyorService) {
        this.conveyorService = conveyorService;
    }

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> getPostOffer(@RequestBody LoanApplicationRequestDTO
                                                                       loanApplicationRequestDTO) {

        try {
            DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
            List<LoanOfferDTO> loanOfferDTOS;
            loanOfferDTOS = conveyorService.getOffers(loanApplicationRequestDTO);

            return new ResponseEntity<>(loanOfferDTOS, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //так как был уже прескоринг и тут указан только скоринг,
    // считаем, что имена и прочее верно передаются
    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> getPostCalculation(@RequestBody ScoringDataDTO scoringDataDTO) {
       /* try {
            return new ResponseEntity<>(, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        return null;
    }
}
