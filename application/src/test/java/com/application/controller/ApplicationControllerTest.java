package com.application.controller;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicationService applicationService;

    @Test
    void whenCreatedApplication_thenReturn200() throws Exception {

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

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    void whenAmountIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenTermIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenFirstNameIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenLastNameIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenEmailIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .birthdate(LocalDate.of(2000, 2, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenBirthdayIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPassportSeriesIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportNumber("123456")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPassportNumberIsNull_thenReturn400() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(6)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 1, 1))
                .passportSeries("1234")
                .build();

        mockMvc.perform(post("/application")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanApplicationRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}
