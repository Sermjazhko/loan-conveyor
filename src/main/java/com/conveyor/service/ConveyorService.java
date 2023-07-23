package com.conveyor.service;

import com.conveyor.dto.LoanApplicationRequestDTO;
import com.conveyor.dto.LoanOfferDTO;

import java.util.List;

public interface ConveyorService {

    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
