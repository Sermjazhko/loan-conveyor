package com.application.controller;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicationService applicationService;

    @Test
    void testWhenCreatedApplication_thenReturn200() throws Exception {

        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(2L)
                .term(10)
                .rate(new BigDecimal("12.0"))
                .requestedAmount(new BigDecimal("12000.00"))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .monthlyPayment(new BigDecimal("1200.00"))
                .build();
        List<LoanOfferDTO> loanOfferDTOS = new ArrayList<>();
        loanOfferDTOS.add(loanOfferDTO);

        Mockito.when(applicationService.createLoanApplication(any())).thenReturn(loanOfferDTOS);
        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void testWhenAmountIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenTermIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenFirstNameIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenLastNameIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenEmailIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .birthdate(LocalDate.of(2000, 2, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenBirthdayIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenPassportSeriesIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenPassportNumberIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .build();

        mockMvc.perform(post("http://localhost:9070/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}
