package com.conveyor.validation;

import com.conveyor.dto.LoanApplicationRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DataValidationTest {

    @Test
    void testCheckDateWhenNull_returnTrow() {
        Throwable exception = assertThrows(NullPointerException.class, () -> {
                    DataValidation.checkDate(null);
                }
        );
        assertEquals("Date must not be null", exception.getMessage());
    }

    @Test
    void testCheckDateWhenAgeLessThan18_returnTrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LocalDate localDate = LocalDate.of(2006, 01, 01);
                    DataValidation.checkDate(localDate);
                }
        );
        assertEquals("Age less than 18 years", exception.getMessage());
    }

    @Test
    void testCheckDate() {
        //вроде сам localDate подразумевает формат uuuu-mm-dd и вывести как LocalDate
        //в другом формате не выйдет (String можно, а вот LocalDate...), даже если писать 200-ый год
        LocalDate localDate = LocalDate.of(2000, 01, 01);
        assertTrue(DataValidation.checkDate(localDate));
    }

    @Test
    void testCheckLoanApplicationRequestDTOWhenIncorrectAmount_thenReturnTrow() {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Amount cannot be null", exception.getMessage());

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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Amount cannot be less 10 000", exception2.getMessage());
    }


    @Test
    void testCheckLoanApplicationRequestDTOWhenIncorrectTerm_thenReturnTrow() {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Term cannot be null", exception.getMessage());

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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Term cannot be less 6", exception2.getMessage());
    }

    @Test
    void testCheckLoanApplicationRequestDTOWhenFirstNameNull_thenReturnTrow() {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("First name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "0123456789012345678901234567890"})
    void testCheckLoanApplicationRequestDTOWhenIncorrectFirstName_thenReturnTrow(String str) {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("First name must be between 2 and 30 characters", exception.getMessage());
    }


    @Test
    void testCheckLoanApplicationRequestDTOWhenLastNameNull_thenReturnTrow() {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Last name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"L", "0123456789012345678901234567890"})
    void testCheckLoanApplicationRequestDTOWhenIncorrectLastName_thenReturnTrow(String str) {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Last name must be between 2 and 30 characters", exception.getMessage());
    }


    @Test
    void testCheckLoanApplicationRequestDTOWhenMiddleNameNull_thenReturnTrue() {

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
        assertTrue(DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO));
    }

    @ParameterizedTest
    @ValueSource(strings = {"M", "0123456789012345678901234567890"})
    void testCheckLoanApplicationRequestDTOWhenIncorrectMiddleName_thenReturnTrow(String str) {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Middle name must be between 2 and 30 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"seldeadmail.ru", "seldead@", "@mail.ru"})
    void testCheckLoanApplicationRequestDTOWhenIncorrectEmail_thenReturnTrow(String str) {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Email address has invalid format: " + str, exception.getMessage());
    }

    @Test
    void testCheckLoanApplicationRequestDTOWhenPassportSeriesNull_thenReturnTrow() {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport series cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "12345", "1", "123456"})
    void testCheckLoanApplicationRequestDTOWhenIncorrectPassportSeries_thenReturnTrow(String str) {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport series must consist 4 characters", exception.getMessage());
    }

    @Test
    void testCheckLoanApplicationRequestDTOWhenPassportNumberNull_thenReturnTrow() {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport number cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567", "1", "12"})
    void testCheckLoanApplicationRequestDTOWhenIncorrectPassportNumber_thenReturnTrow(String str) {
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
                    DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO);
                }
        );
        assertEquals("Passport number must consist 6 characters", exception.getMessage());
    }

    @Test
    void testCheckLoanApplicationRequestDTO() {
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
        assertTrue(DataValidation.checkLoanApplicationRequestDTO(loanApplicationRequestDTO));
    }
}