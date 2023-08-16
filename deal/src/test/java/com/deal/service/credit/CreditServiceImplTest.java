package com.deal.service.credit;

import com.deal.dto.CreditDTO;
import com.deal.dto.EmploymentDTO;
import com.deal.dto.FinishRegistrationRequestDTO;
import com.deal.dto.ScoringDataDTO;
import com.deal.entities.CreditStatus;
import com.deal.entities.Gender;
import com.deal.entities.MaritalStatus;
import com.deal.model.Client;
import com.deal.model.Credit;
import com.deal.model.Passport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @InjectMocks
    private CreditServiceImpl creditService;

    @Autowired
    private CreditRepository creditRepository;

    @Test
    public void testAddAndGetCredit() {
        //первые два метода
        Credit credit = Credit.builder()
                .amount(new BigDecimal("12000"))
                .creditStatus(CreditStatus.ISSUED)
                .salaryClient(true)
                .insuranceEnable(true)
                .psk(new BigDecimal("3.4"))
                .term(12)
                .rate(new BigDecimal("12"))
                .monthlyPayment(new BigDecimal("1211"))
                .paymentSchedule("{}")
                .build();

        Long id = creditRepository.save(credit).getId();
        Credit testCredit = creditRepository.findById(id).get();

        assertEquals(testCredit.getId(), id);
        assertEquals(testCredit.getCreditStatus(), CreditStatus.ISSUED);
        assertEquals(testCredit.getAmount(), new BigDecimal("12000"));
        assertEquals(testCredit.getTerm().longValue(), 12);
        assertEquals(testCredit.getInsuranceEnable(), true);
        assertEquals(testCredit.getSalaryClient(), true);
        assertEquals(testCredit.getPaymentSchedule(), "{}");
        assertEquals(testCredit.getMonthlyPayment(), new BigDecimal("1211"));
        assertEquals(testCredit.getRate(), new BigDecimal("12"));
        assertEquals(testCredit.getPsk(), new BigDecimal("3.4"));
    }

    @Test
    public void testCreateScoringData() {
        Passport passport = Passport.builder()
                .number("2121")
                .series("212121")
                .issueBranch("??")
                .issueDate(LocalDate.of(2014, 1, 1))
                .build();

        Jsonb jsonb = JsonbBuilder.create();
        String strPassport = jsonb.toJson(passport);

        Client client = Client.builder()
                .firstName("asd")
                .lastName("dasd")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .dependentAmount(2)
                .passport(strPassport)
                .account("0000S")
                .build();

        FinishRegistrationRequestDTO finishRegistrationRequestDTO =
                FinishRegistrationRequestDTO.builder()
                        .account("0000S")
                        .dependentAmount(2)
                        .employment(new EmploymentDTO())
                        .gender(Gender.MALE)
                        .maritalStatus(MaritalStatus.MARRIED)
                        .passportIssueBrach(passport.getIssueBranch())
                        .passportIssueDate(passport.getIssueDate())
                        .build();

        ScoringDataDTO scoringDataDTO = creditService.createScoringData(finishRegistrationRequestDTO, client);

        assertEquals(scoringDataDTO.getBirthdate(), client.getBirthday());
        assertEquals(scoringDataDTO.getFirstName(), client.getFirstName());
        assertEquals(scoringDataDTO.getLastName(), client.getLastName());
        assertEquals(scoringDataDTO.getBirthdate(), client.getBirthday());
        assertEquals(scoringDataDTO.getPassportNumber(), passport.getNumber());
        assertEquals(scoringDataDTO.getPassportSeries(), passport.getSeries());
        assertEquals(scoringDataDTO.getPassportIssueBranch(), passport.getIssueBranch());
        assertEquals(scoringDataDTO.getPassportIssueDate(), passport.getIssueDate());
        assertEquals(scoringDataDTO.getAccount(), finishRegistrationRequestDTO.getAccount());
        assertEquals(scoringDataDTO.getDependentAmount(), finishRegistrationRequestDTO.getDependentAmount());
        assertEquals(scoringDataDTO.getEmployment(), finishRegistrationRequestDTO.getEmployment());
        assertEquals(scoringDataDTO.getGender(), finishRegistrationRequestDTO.getGender());
        //заглушки
        assertEquals(scoringDataDTO.getTerm().intValue(), 12);
        assertEquals(scoringDataDTO.getAmount(), new BigDecimal("10000"));
        assertEquals(scoringDataDTO.getIsSalaryClient(), true);
        assertEquals(scoringDataDTO.getIsInsuranceEnabled(), true);
    }

    @Test
    public void testCreateCredit() {
        List list = new ArrayList<>();
        list.add("{}");
        CreditDTO creditDTO = CreditDTO.builder()
                .amount(new BigDecimal("12000"))
                .isSalaryClient(true)
                .isInsuranceEnabled(true)
                .psk(new BigDecimal("3.4"))
                .term(12)
                .rate(new BigDecimal("12"))
                .monthlyPayment(new BigDecimal("1211"))
                .paymentSchedule(list)
                .build();

        Credit credit = creditService.createCredit(creditDTO);

        assertEquals(credit.getCreditStatus(), CreditStatus.CALCULATE);
        assertEquals(credit.getAmount(), new BigDecimal("12000"));
        assertEquals(credit.getTerm().longValue(), 12);
        assertEquals(credit.getInsuranceEnable(), true);
        assertEquals(credit.getSalaryClient(), true);
        assertEquals(credit.getPaymentSchedule(), "[\"{}\"]");
        assertEquals(credit.getMonthlyPayment(), new BigDecimal("1211"));
        assertEquals(credit.getRate(), new BigDecimal("12"));
        assertEquals(credit.getPsk(), new BigDecimal("3.4"));
    }
/*
    @AfterEach
    public void resetDb() {
        creditRepository.deleteAll();
    }*/
}