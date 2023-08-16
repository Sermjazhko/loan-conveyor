package com.deal.service.client;

import com.deal.dto.LoanApplicationRequestDTO;
import com.deal.entities.Gender;
import com.deal.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testAddAndGetClient() {
        //первые два метода
        Client client = Client.builder()
                .firstName("asd")
                .lastName("dasd")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .dependentAmount(2)
                .passport("{\"number\": 2000}")
                .account("0000S")
                .build();

        System.out.println("GOOD");
        Long id = clientRepository.save(client).getId();
        Client testClient = clientRepository.findById(id).get();

        System.out.println("GOOD");
        assertEquals(testClient.getId(), id);
        assertEquals(testClient.getBirthday(), LocalDate.of(2000, 1, 1));
        assertEquals(testClient.getFirstName(), "asd");
        assertEquals(testClient.getLastName(), "dasd");
        assertEquals(testClient.getAccount(), "0000S");
        assertEquals(testClient.getDependentAmount().intValue(), 2);
        assertEquals(testClient.getGender(), Gender.MALE);
        assertEquals(testClient.getPassport(), "{\"number\": 2000}");

        clientRepository.deleteById(id);
    }

    @Test
    public void testCreatePassport() {
        LoanApplicationRequestDTO loanApplicationRequestDTO =
                LoanApplicationRequestDTO.builder()
                        .passportSeries("1212")
                        .passportNumber("121212")
                        .build();
        String testPassport = clientService.createPassport(loanApplicationRequestDTO);
        String passport = "{\"number\":\"121212\",\"series\":\"1212\"}";
        assertEquals(passport, testPassport);
    }

    @Test
    public void testCreateEmployment() {
        LoanApplicationRequestDTO loanApplicationRequestDTO =
                LoanApplicationRequestDTO.builder()
                        .build();
        String testEmployment = clientService.createEmployment(loanApplicationRequestDTO);
        String employment = "{\"salary\":10000,\"workExperienceCurrent\":12,\"workExperienceTotal\":12}";
        assertEquals(employment, testEmployment);
    }

    @Test
    public void testCreateClient() {
        LocalDate localDate = LocalDate.of(2000, 2, 2);
        LoanApplicationRequestDTO loanApplicationRequestDTO =
                LoanApplicationRequestDTO.builder()
                        .firstName("alex")
                        .lastName("dm")
                        .middleName("net")
                        .birthdate(localDate)
                        .email("sel@mail.ru")
                        .passportSeries("1212")
                        .passportNumber("121212")
                        .build();

        String passport = clientService.createPassport(loanApplicationRequestDTO);
        String employment = clientService.createEmployment(loanApplicationRequestDTO);

        Client client = clientService.createClient(loanApplicationRequestDTO, passport, employment);
        assertEquals(employment, client.getEmployment());
        assertEquals(passport, client.getPassport());
        assertEquals("alex", client.getFirstName());
        assertEquals("dm", client.getLastName());
        assertEquals("net", client.getMiddleName());
        assertEquals("sel@mail.ru", client.getEmail());
        assertEquals("0000", client.getAccount());
    }
}