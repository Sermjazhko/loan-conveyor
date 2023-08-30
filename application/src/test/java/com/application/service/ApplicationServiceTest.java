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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void testWhenLoanApplicationIsCorrect_createApplication() {
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
                    Mockito.verify(dealService, Mockito.times(1)).createLoanApplication(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void testWhenLoanApplicationIsIncorrect_notCreateApplication() {
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
                    Mockito.verify(dealService, Mockito.times(0)).createLoanApplication(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void testApplyOffer() {
        assertDoesNotThrow(() -> {
                    LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
                    applicationService.applyOffer(loanOfferDTO);
                    Mockito.verify(dealService, Mockito.times(1)).applyOffer(loanOfferDTO);
                }
        );
    }

    @Test
    void testWhenAmountLessPrescoringAmount_returnTrow() {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Amount cannot be less 10 000", exception.getMessage());
    }

    @Test
    void testWhenTermLessPrescoringTerm_returnTrow() {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Term cannot be less 6", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "0123456789012345678901234567890"})
    void testWhenFirstNameIsIncorrect_returnTrow(String str) {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("First name must be between 2 and 30 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"L", "0123456789012345678901234567890"})
    void testWhenLastNameIsIncorrect_thenReturnTrow(String str) {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Last name must be between 2 and 30 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"M", "0123456789012345678901234567890"})
    void testWhenMiddleNameIsIncorrect_thenReturnTrow(String str) {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Middle name must be between 2 and 30 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"seldeadmail.ru", "seldead@", "@mail.ru"})
    void testWhenEmailIsIncorrect_thenReturnTrow(String str) {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Email address has invalid format. Does not match the pattern: ^[A-Za-z0-9+_.-]+@(.+)$", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "12345", "1", "123456"})
    void testWhenPassportSeriesIsIncorrect_thenReturnTrow(String str) {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport series must consist 4 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567", "1", "12"})
    void testWhenPassportNumberIsIncorrect_thenReturnTrow(String str) {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport number must consist 6 characters", exception.getMessage());
    }

    @Test
    void testWhenLoanApplicationIsCorrect_thenReturnTrue() {
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

                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
    }

    @Test
    void testWhenAgeLessThan18_returnTrow() {
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
                    applicationService.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Age less than 18 years", exception.getMessage());
    }
}