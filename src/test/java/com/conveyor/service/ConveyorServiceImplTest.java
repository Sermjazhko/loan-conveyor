package com.conveyor.service;

import com.conveyor.dto.*;
import com.conveyor.scoring.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConveyorServiceImplTest {

    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private ConveyorServiceImpl conveyorService;

    @Test
    void testGetOffers() throws IOException {
        //просто проверка на то, что все данные пришли, вызвались и вышли
        //в лекциях сплошной тест для корректности создания, поэтому без разбивки на разные тесты
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();

        BigDecimal amount = new BigDecimal("100000");
        loanApplicationRequestDTO.setAmount(amount);
        loanApplicationRequestDTO.setTerm(Integer.valueOf(10));

        BigDecimal totalAmountTrue = new BigDecimal("100005");
        BigDecimal totalAmountFalse = new BigDecimal("100010");
        BigDecimal rateFF = new BigDecimal("13");
        BigDecimal rateFT = new BigDecimal("12");
        BigDecimal rateTF = new BigDecimal("11");
        BigDecimal rateTT = new BigDecimal("10");
        BigDecimal monthlyPayment = new BigDecimal("20000");

        //мок вызова метода
        when(scoringService.totalAmountByServices(any(), any())).thenReturn(totalAmountFalse, totalAmountFalse,
                totalAmountTrue, totalAmountTrue);
        when(scoringService.calculateRate(any(), any())).thenReturn(rateFF, rateFT, rateTF, rateTT);
        when(scoringService.getAnnuityPayment(any(), any(), any())).thenReturn(monthlyPayment);

        List<LoanOfferDTO> offers = conveyorService.getOffers(loanApplicationRequestDTO);

        assertEquals(4, offers.size());
        assertEquals(amount, offers.get(0).getRequestedAmount());
        assertEquals(Long.valueOf(1L), offers.get(1).getApplicationId());
        assertEquals(Integer.valueOf(10), offers.get(0).getTerm());

        Mockito.verify(scoringService, Mockito.times(4)).totalAmountByServices(any(), any());
        Mockito.verify(scoringService, Mockito.times(4)).calculateRate(any(), any());
        Mockito.verify(scoringService, Mockito.times(4)).getAnnuityPayment(any(), any(), any());

        assertEquals(totalAmountFalse, offers.get(0).getTotalAmount());
        assertEquals(totalAmountFalse, offers.get(1).getTotalAmount());
        assertEquals(totalAmountTrue, offers.get(2).getTotalAmount());
        assertEquals(totalAmountTrue, offers.get(3).getTotalAmount());

        assertEquals(rateFF, offers.get(0).getRate());
        assertEquals(rateFT, offers.get(1).getRate());
        assertEquals(rateTF, offers.get(2).getRate());
        assertEquals(rateTT, offers.get(3).getRate());

        assertEquals(monthlyPayment, offers.get(0).getMonthlyPayment());
    }

    @Test
    void testGetOffersWhenNull_thenThrow() {
        assertThrows(NullPointerException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = null;
                    List<LoanOfferDTO> offers = conveyorService.getOffers(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void testGetCalculation() throws IOException {
        //просто проверка на то, что все данные пришли, вызвались и вышли
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();

        BigDecimal amount = new BigDecimal("100000");
        Integer term = 12;
        String account = "1234", firstName = "Leks", lastName = "Ll", middleName = "Dd",
                passportSeries = "1212", passportNumber = "123456", passportIssueBranch = "...";
        LocalDate birthdate = LocalDate.of(2000, 12, 10), passportIssueDate =
                LocalDate.of(2020, 12, 12);
        Gender gender = Gender.MAN;
        MaritalStatus maritalStatus = MaritalStatus.MARRIED;
        EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED, "123123134",
                BigDecimal.valueOf(50000),
                Position.MIDDLE_MANAGER, 20, 20);


        scoringDataDTO.setAmount(amount);
        scoringDataDTO.setAccount(account);
        scoringDataDTO.setBirthdate(birthdate);
        scoringDataDTO.setTerm(term);
        scoringDataDTO.setFirstName(firstName);
        scoringDataDTO.setLastName(lastName);
        scoringDataDTO.setMiddleName(middleName);
        scoringDataDTO.setGender(gender);
        scoringDataDTO.setPassportSeries(passportSeries);
        scoringDataDTO.setPassportNumber(passportNumber);
        scoringDataDTO.setPassportIssueDate(passportIssueDate);
        scoringDataDTO.setPassportIssueBranch(passportIssueBranch);
        scoringDataDTO.setMaritalStatus(maritalStatus);
        scoringDataDTO.setDependentAmount(1);
        scoringDataDTO.setEmployment(employmentDTO);
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(true);

        BigDecimal insurance = new BigDecimal("1000");
        BigDecimal baseRate = new BigDecimal("10");
        BigDecimal rate = new BigDecimal("7");
        BigDecimal monthlyPayment = new BigDecimal("2000");
        BigDecimal psk = new BigDecimal("2");
        List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<PaymentScheduleElement>() {
            {
                add(new PaymentScheduleElement());
                add(new PaymentScheduleElement());
            }
        };

        when(scoringService.getBaseRateAndInsurance()).thenReturn(
                new ArrayList<BigDecimal>() {
                    {
                        add(baseRate);
                        add(insurance);
                    }
                }
        );
        when(scoringService.getAnnuityPayment(any(), any(), any())).thenReturn(monthlyPayment);
        when(scoringService.getPSK(any(), any(), any())).thenReturn(psk);
        when(scoringService.scoringRate(any(), any())).thenReturn(rate);
        when(scoringService.createListPayment(any(), any(), any())).thenReturn(paymentScheduleElements);
        when(scoringService.checkScoringDataDTO(any(), any())).thenReturn(true);

        BigDecimal requestedAmount = amount.add(insurance);

        CreditDTO creditDTO = conveyorService.getCalculation(scoringDataDTO);

        Mockito.verify(scoringService, Mockito.times(1)).getBaseRateAndInsurance();
        Mockito.verify(scoringService, Mockito.times(1)).scoringRate(any(), any());
        Mockito.verify(scoringService, Mockito.times(1)).getAnnuityPayment(any(), any(), any());
        Mockito.verify(scoringService, Mockito.times(1)).getPSK(any(), any(), any());
        Mockito.verify(scoringService, Mockito.times(1)).createListPayment(any(), any(), any());
        Mockito.verify(scoringService, Mockito.times(1)).checkScoringDataDTO(any(), any());

        assertEquals(requestedAmount, creditDTO.getAmount());
        assertEquals(term, creditDTO.getTerm());
        assertEquals(monthlyPayment, creditDTO.getMonthlyPayment());
        assertEquals(rate, creditDTO.getRate());
        assertEquals(psk, creditDTO.getPsk());
        assertEquals(true, creditDTO.getIsInsuranceEnabled());
        assertEquals(true, creditDTO.getIsSalaryClient());
        assertEquals(paymentScheduleElements, creditDTO.getPaymentSchedule());
    }

    @Test
    void testGetCalculationWhenNull_thenThrow() {
        assertThrows(NullPointerException.class, () -> {
                    ScoringDataDTO scoringDataDTO = null;
                    when(scoringService.getBaseRateAndInsurance()).thenReturn(
                            new ArrayList<BigDecimal>() {
                                {
                                    add(new BigDecimal("0"));
                                    add(new BigDecimal("0"));
                                }
                            }
                    );
                    conveyorService.getCalculation(scoringDataDTO);
                }
        );
    }
}