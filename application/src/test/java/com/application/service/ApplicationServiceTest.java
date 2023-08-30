package com.application.service;

import com.application.dto.LoanApplicationRequestDTO;
import com.application.dto.LoanOfferDTO;
import com.application.exception.PrescoringException;
import com.application.integration.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void whenLoanApplicationIsCorrect_createApplication() {
        assertDoesNotThrow(() -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(6)
                            .firstName("First")
                            .lastName("Last")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                    verify(dealService).createLoanApplication(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void whenLoanApplicationIsIncorrect_notCreateApplication() {
        assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(6)
                            .firstName("F")
                            .lastName("Last")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                    verify(dealService, never()).createLoanApplication(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void applyOffer() {
        assertDoesNotThrow(() -> {
                    LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
                    applicationService.applyOffer(loanOfferDTO);
                    verify(dealService).applyOffer(loanOfferDTO);
                }
        );
    }

    @Test
    void whenAmountLessPrescoringAmount_returnTrow() {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("9999.99"))
                            .term(6)
                            .firstName("First")
                            .lastName("Last")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @Test
    void whenTermLessPrescoringTerm_returnTrow() {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(5)
                            .firstName("First")
                            .lastName("Last")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "0123456789012345678901234567890"})
    void whenFirstNameIsIncorrect_returnTrow(String str) {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(6)
                            .firstName(str)
                            .lastName("Last")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"L", "0123456789012345678901234567890"})
    void whenLastNameIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName(str)
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"M", "0123456789012345678901234567890"})
    void whenMiddleNameIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName(str)
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"seldeadmail.ru", "seldead@", "@mail.ru"})
    void whenEmailIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email(str)
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "12345", "1", "123456"})
    void whenPassportSeriesIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries(str)
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567", "1", "12"})
    void whenPassportNumberIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber(str)
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }

    @Test
    void whenLoanApplicationIsCorrect_thenReturnTrue() {
        assertDoesNotThrow(() -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();

                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void whenAgeLessThan18_returnTrow() {
        Throwable exception = assertThrows(PrescoringException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(6)
                            .firstName("First")
                            .lastName("Last")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2006, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    applicationService.createLoanApplication(loanApplicationRequestDTO);
                }
        );
        assertEquals("Prescoring error", exception.getMessage());
    }
}