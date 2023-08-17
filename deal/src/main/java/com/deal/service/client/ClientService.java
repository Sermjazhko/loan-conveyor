package com.deal.service.client;

import com.deal.dto.LoanApplicationRequestDTO;
import com.deal.model.Client;

public interface ClientService {

    void addClientToDB(Client client);

    Client getClientById(Long id);

    String createPassport(LoanApplicationRequestDTO loanApplicationRequestDTO);
/*
    String createEmployment(LoanApplicationRequestDTO loanApplicationRequestDTO);*/

    Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO, String passport);
}
