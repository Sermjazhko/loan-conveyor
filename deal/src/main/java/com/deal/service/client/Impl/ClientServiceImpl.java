package com.deal.service.client.Impl;

import com.deal.dto.LoanApplicationRequestDTO;
import com.deal.model.Client;
import com.deal.model.Passport;
import com.deal.repository.ClientRepository;
import com.deal.service.client.ClientService;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void addClientToDB(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client getClientById(Long id) {

        return clientRepository.findById(id).get();
    }

    @Override
    public String createPassport(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Jsonb jsonb = JsonbBuilder.create();
        Passport passport = Passport.builder()
                .series(loanApplicationRequestDTO.getPassportSeries())
                .number(loanApplicationRequestDTO.getPassportNumber())
                .build();
        return jsonb.toJson(passport);
    }

    @Override
    public Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO, String passport) {
        return Client.builder()
                .firstName(loanApplicationRequestDTO.getFirstName())
                .lastName(loanApplicationRequestDTO.getLastName())
                .middleName(loanApplicationRequestDTO.getMiddleName())
                .birthday(loanApplicationRequestDTO.getBirthdate())
                .email(loanApplicationRequestDTO.getEmail())
                .passport(passport)
                .build();
    }
}
