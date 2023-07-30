package com.conveyor.service;

import com.conveyor.dto.CreditDTO;
import com.conveyor.dto.LoanApplicationRequestDTO;
import com.conveyor.dto.LoanOfferDTO;
import com.conveyor.dto.ScoringDataDTO;

import java.io.IOException;
import java.util.List;


public interface ConveyorService {

    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) throws IOException;

    CreditDTO getCalculation(ScoringDataDTO scoringDataDTO) throws IOException;
}
