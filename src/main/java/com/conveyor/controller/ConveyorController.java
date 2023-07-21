package com.conveyor.controller;

import com.conveyor.dto.CreditDTO;
import com.conveyor.dto.LoanApplicationRequestDTO;
import com.conveyor.dto.LoanOfferDTO;
import com.conveyor.dto.ScoringDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
public class ConveyorController {

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> saveBuyer(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO){
       /* try {
            return new ResponseEntity<>(buyerRepository.save(buyer), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        return null;
    }

    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> saveBuyer(@RequestBody ScoringDataDTO scoringDataDTO){
       /* try {
            return new ResponseEntity<>(buyerRepository.save(buyer), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        return null;
    }
}
