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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DealResponseServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DealResponseService dealResponseService;

    @Test
    void testGetResultPostRequestOffer() {
        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder()
                .applicationId(1L)
                .term(12)
                .build();
        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder()
                .applicationId(2L)
                .term(10)
                .build();


        LoanOfferDTO[] listLoan = new LoanOfferDTO[2];
        listLoan[0] = loanOfferDTO1;
        listLoan[1] = loanOfferDTO2;

        Mockito.when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class))).
                thenReturn(listLoan);

        List<LoanOfferDTO> list = dealResponseService.getResultPostRequestOffer(new LoanApplicationRequestDTO());

        Mockito.verify(restTemplate, Mockito.times(1)).
                postForObject(any(String.class), any(Object.class), any(Class.class));

        assertEquals(loanOfferDTO1.getApplicationId(), list.get(0).getApplicationId());
        assertEquals(loanOfferDTO2.getApplicationId(), list.get(1).getApplicationId());
        assertEquals(loanOfferDTO1.getTerm(), list.get(0).getTerm());
        assertEquals(loanOfferDTO2.getTerm(), list.get(1).getTerm());
    }

    @Test
    void testGetResultPutRequestCalculation() {
        dealResponseService.getResultPutRequestCalculation(new LoanOfferDTO());
        Mockito.verify(restTemplate, Mockito.times(1)).put(any(String.class), any(Object.class));
    }
}