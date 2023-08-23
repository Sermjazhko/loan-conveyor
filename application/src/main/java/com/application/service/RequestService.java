package com.application.service;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;

import java.util.List;

public interface RequestService {

    List<LoanOfferDTO> getResultPostRequestOffer (LoanApplicationRequestDTO loanApplicationRequestDTO);

    boolean getResultPutRequestCalculation(LoanOfferDTO loanOfferDTO);
}
