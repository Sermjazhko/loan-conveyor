package com.deal.service.client.Impl;

import com.deal.dto.LoanApplicationRequestDTO;
import com.deal.enums.Gender;
import com.deal.model.Client;
import com.deal.repository.ClientRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(
        locations = "classpath:application-local.properties")
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Disabled
    @Test
    public void testAddAndGetClient() {
        //первые два метода
        Client client = Client.builder()
                .firstName("asd")
                .lastName("dasd")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .email("seldead@m.ru")
                .dependentAmount(2)
                .passport("{\"number\": 2000}")
                .account("0000S")
                .build();

        Long id = clientRepository.save(client).getId();
        Client testClient = clientRepository.findById(id).get();

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

    @Disabled
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

    @Disabled
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

        Client client = clientService.createClient(loanApplicationRequestDTO, passport);

        assertEquals(passport, client.getPassport());
        assertEquals("alex", client.getFirstName());
        assertEquals("dm", client.getLastName());
        assertEquals("net", client.getMiddleName());
        assertEquals("sel@mail.ru", client.getEmail());
        assertEquals(null, client.getAccount());
        assertEquals(null, client.getEmployment());
        assertEquals(null, client.getGender());
        assertEquals(null, client.getMaritalStatus());
        assertEquals(null, client.getDependentAmount());
    }
}