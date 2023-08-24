package com.application.service;

import com.application.dto.LoanApplicationRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrescoringTest {

    @Test
    void testWhenDateIsNull_returnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    Prescoring.isValidDate(null);
                }
        );
        assertEquals("Date must not be null", exception.getMessage());
    }

    @Test
    void testWhenAgeLessThan18_returnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LocalDate localDate = LocalDate.of(2006, 01, 01);
                    Prescoring.isValidDate(localDate);
                }
        );
        assertEquals("Age less than 18 years", exception.getMessage());
    }

    @Test
    void testIsDateValid() {
        LocalDate localDate = LocalDate.of(2000, 01, 01);
        assertTrue(Prescoring.isValidDate(localDate));
    }

    @Test
    void testWhenAmountIsNull_thenReturnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    void testWhenAmountLessPrescoringAmount_thenReturnTrow() {
        Throwable exception2 = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("9999"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Amount cannot be less 10 000", exception2.getMessage());
    }


    @Test
    void testWhenTermIsNull_thenReturnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Term cannot be null", exception.getMessage());
    }

    @Test
    void testWhenTermLessPrescoringTerm_thenReturnTrow() {
        Throwable exception2 = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(5)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Term cannot be less 6", exception2.getMessage());
    }

    @Test
    void testWhenFirstNameIsNull_thenReturnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("First name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "0123456789012345678901234567890"})
    void testWhenFirstNameIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName(str)
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("First name must be between 2 and 30 characters", exception.getMessage());
    }


    @Test
    void testWhenLastNameIsNull_thenReturnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Last name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"L", "0123456789012345678901234567890"})
    void testWhenLastNameIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
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
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Last name must be between 2 and 30 characters", exception.getMessage());
    }


    @Test
    void testWhenMiddleNameIsNull_thenReturnTrue() {

        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(new BigDecimal("10000"))
                .term(10)
                .firstName("First")
                .lastName("Last")
                .email("seldead@mail.ru")
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertTrue(Prescoring.calculatePrescoring(loanApplicationRequestDTO));
    }

    @ParameterizedTest
    @ValueSource(strings = {"M", "0123456789012345678901234567890"})
    void testWhenMiddleNameIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
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
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Middle name must be between 2 and 30 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"seldeadmail.ru", "seldead@", "@mail.ru"})
    void testWhenEmailIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
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
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Email address has invalid format: " + str, exception.getMessage());
    }

    @Test
    void testWhenPassportSeriesIsNull_thenReturnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportNumber("123456")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport series cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "12345", "1", "123456"})
    void testWhenPassportSeriesIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
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
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport series must consist 4 characters", exception.getMessage());
    }

    @Test
    void testWhenPassportNumberIsNull_thenReturnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                            .amount(new BigDecimal("10000"))
                            .term(10)
                            .firstName("First")
                            .lastName("Last")
                            .middleName("Middle")
                            .email("seldead@mail.ru")
                            .birthdate(LocalDate.of(2000, 01, 01))
                            .passportSeries("1234")
                            .build();
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport number cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567", "1", "12"})
    void testWhenPassportNumberIsIncorrect_thenReturnTrow(String str) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
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
                    Prescoring.calculatePrescoring(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport number must consist 6 characters", exception.getMessage());
    }

    @Test
    void testWhenLoanApplicationIsCorrect_thenReturnTrue() {
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
        assertTrue(Prescoring.calculatePrescoring(loanApplicationRequestDTO));
    }
}