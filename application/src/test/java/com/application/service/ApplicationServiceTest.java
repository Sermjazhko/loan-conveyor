package com.application.service;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void testCreateLoanApplication() {
        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder()
                .applicationId(1L)
                .term(12)
                .rate(new BigDecimal("10.0"))
                .requestedAmount(new BigDecimal("10000.00"))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .monthlyPayment(new BigDecimal("1000.00"))
                .build();
        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder()
                .applicationId(2L)
                .term(10)
                .rate(new BigDecimal("12.0"))
                .requestedAmount(new BigDecimal("12000.00"))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .monthlyPayment(new BigDecimal("1200.00"))
                .build();

        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .firstName("First")
                .lastName("Last")
                .middleName("Middle")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        LoanOfferDTO[] listLoan = new LoanOfferDTO[2];
        listLoan[0] = loanOfferDTO1;
        listLoan[1] = loanOfferDTO2;

        Mockito.when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class))).
                thenReturn(listLoan);

        List<LoanOfferDTO> list = applicationService.createLoanApplication(loanApplicationRequestDTO);

        Mockito.verify(restTemplate, Mockito.times(1)).
                postForObject(any(String.class), any(Object.class), any(Class.class));

        assertEquals(loanOfferDTO1.getApplicationId(), list.get(0).getApplicationId());
        assertEquals(loanOfferDTO1.getTerm(), list.get(0).getTerm());
        assertEquals(loanOfferDTO1.getRate(), list.get(0).getRate());
        assertEquals(loanOfferDTO1.getRequestedAmount(), list.get(0).getRequestedAmount());
        assertEquals(loanOfferDTO1.getIsInsuranceEnabled(), list.get(0).getIsInsuranceEnabled());
        assertEquals(loanOfferDTO1.getIsSalaryClient(), list.get(0).getIsSalaryClient());
        assertEquals(loanOfferDTO1.getMonthlyPayment(), list.get(0).getMonthlyPayment());


        assertEquals(loanOfferDTO2.getApplicationId(), list.get(1).getApplicationId());
        assertEquals(loanOfferDTO2.getTerm(), list.get(1).getTerm());
        assertEquals(loanOfferDTO2.getRate(), list.get(1).getRate());
        assertEquals(loanOfferDTO2.getRequestedAmount(), list.get(1).getRequestedAmount());
        assertEquals(loanOfferDTO2.getIsInsuranceEnabled(), list.get(1).getIsInsuranceEnabled());
        assertEquals(loanOfferDTO2.getIsSalaryClient(), list.get(1).getIsSalaryClient());
        assertEquals(loanOfferDTO2.getMonthlyPayment(), list.get(1).getMonthlyPayment());
    }

    @Test
    void testApplyOffer() {
        applicationService.applyOffer(new LoanOfferDTO());
        Mockito.verify(restTemplate, Mockito.times(1)).put(any(String.class), any(Object.class));
    }
}