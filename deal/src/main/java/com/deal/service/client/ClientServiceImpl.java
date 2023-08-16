package com.deal.service.client;

import com.deal.dto.LoanApplicationRequestDTO;
import com.deal.model.Client;
import com.deal.model.Employment;
import com.deal.model.Passport;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.math.BigDecimal;

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
                //.issueBranch("...")
                //.issueDate(LocalDate.of(2000, 10, 10))
                .build();
        return jsonb.toJson(passport);
    }

    @Override
    public String createEmployment(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Jsonb jsonb = JsonbBuilder.create();
        Employment employment = Employment.builder()
                //.employmentStatus(EmploymentStatus.INCORRECT_EMPLOYMENT_STATUS)
                //.employer_inn("???")
                .salary(new BigDecimal("10000"))
                //.position(Position.INCORRECT_POSITION)
                .workExperienceTotal(12)
                .workExperienceCurrent(12)
                .build();
        return jsonb.toJson(employment);
    }

    @Override
    public Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO, String passport, String employment) {
        return Client.builder()
                .firstName(loanApplicationRequestDTO.getFirstName())
                .lastName(loanApplicationRequestDTO.getLastName())
                .middleName(loanApplicationRequestDTO.getMiddleName())
                .birthday(loanApplicationRequestDTO.getBirthdate())
                .email(loanApplicationRequestDTO.getEmail())
                //.gender(Gender.INCORRECT_GENDER)
                //.maritalStatus(MaritalStatus.INCORRECT_MARITAL_STATUS)
                //.dependentAmount(1)
                .passport(passport)
                .employment(employment)
                .account("0000")
                .build();
    }
}
