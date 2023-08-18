package com.deal.service.credit;

import com.deal.dto.CreditDTO;
import com.deal.dto.FinishRegistrationRequestDTO;
import com.deal.dto.ScoringDataDTO;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.model.Credit;

public interface CreditService {

    void addCreditToDB(Credit credit);

    ScoringDataDTO createScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                     Client client, Application application);

    Credit createCredit(CreditDTO creditDTO);
}
