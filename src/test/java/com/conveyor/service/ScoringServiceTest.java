package com.conveyor.service;

import com.conveyor.dto.EmploymentDTO;
import com.conveyor.dto.PaymentScheduleElement;
import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.scoring.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @InjectMocks
    private ScoringService scoringService;

    @Test
    void testGetEmploymentStatusWhenSELF_EMPLOYED_thenReturn1() {
        BigDecimal rate = scoringService.getEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        assertEquals(rate, new BigDecimal("1"));
    }

    @Test
    void testGetEmploymentStatusWhenBUSINESS_thenReturn1() {
        BigDecimal rate = scoringService.getEmploymentStatus(EmploymentStatus.BUSINESS);
        assertEquals(rate, new BigDecimal("3"));
    }

    @Test
    void testGetEmploymentStatusWhenOther_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    scoringService.getEmploymentStatus(EmploymentStatus.INCORRECT_EMPLOYMENT_STATUS);
                }
        );
        assertEquals("Incorrect employment status", exception.getMessage());
    }


    @Test
    void testGetPositionWhenMANAGER_thenReturn0() {
        BigDecimal rate = scoringService.getPosition(Position.MANAGER);
        assertEquals(rate, new BigDecimal("0"));
    }

    @Test
    void testGetPositionWhenMIDDLE_MANAGER_thenReturnMin2() {
        BigDecimal rate = scoringService.getPosition(Position.MIDDLE_MANAGER);
        assertEquals(rate, new BigDecimal("-2"));
    }

    @Test
    void testGetPositionWhenTOP_MANAGER_thenReturnMin4() {
        BigDecimal rate = scoringService.getPosition(Position.TOP_MANAGER);
        assertEquals(rate, new BigDecimal("-4"));
    }

    @Test
    void testGetPositionWhenOther_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    scoringService.getPosition(Position.INCORRECT_POSITION);
                }
        );
        assertEquals("Incorrect position", exception.getMessage());
    }

    @Test
    void testGetMaritalStatusWhenMARRIED_thenReturnMin3() {
        BigDecimal rate = scoringService.getMaritalStatus(MaritalStatus.MARRIED);
        assertEquals(rate, new BigDecimal("-3"));
    }

    @Test
    void testGetMaritalStatusWhenNOT_MARRIED_thenReturnMin1() {
        BigDecimal rate = scoringService.getMaritalStatus(MaritalStatus.NOT_MARRIED);
        assertEquals(rate, new BigDecimal("-1"));
    }

    @Test
    void testGetMaritalStatusWhenOther_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    scoringService.getMaritalStatus(MaritalStatus.INCORRECT_MARITAL_STATUS);
                }
        );
        assertEquals("Incorrect marital status", exception.getMessage());
    }

    @Test
    void testGetDependentAmountWhenMore1_then1() {
        Integer dependentAmount = 3;
        BigDecimal rate = scoringService.getDependentAmount(dependentAmount);
        assertEquals(new BigDecimal("1"), rate);
    }

    @Test
    void testGetDependentAmountWhenLess1_then0() {
        Integer dependentAmount = 1;
        BigDecimal rate = scoringService.getDependentAmount(dependentAmount);
        assertEquals(new BigDecimal("0"), rate);
    }


    @ParameterizedTest
    @ValueSource(ints = {1988, 1980, 1970, 1963})
    void testGetGenderWhenSuitableWOMAN_thenReturnMin3(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.WOMAN, birthday);
        assertEquals(rate, new BigDecimal("-3"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1990, 1989, 1962, 1950})
    void testGetGenderWhenUnsuitableWOMAN_thenReturn0(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.WOMAN, birthday);
        assertEquals(rate, new BigDecimal("0"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1993, 1980, 1970, 1968})
    void testGetGenderWhenSuitableMAN_thenReturnMin3(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.MAN, birthday);
        assertEquals(rate, new BigDecimal("-3"));
    }

    @ParameterizedTest
    @ValueSource(ints = {2000, 1994, 1967, 1950})
    void testGetGenderWhenUnsuitableMAN_thenReturn0(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.MAN, birthday);
        assertEquals(rate, new BigDecimal("0"));
    }

    @Test
    void testGetGenderWhenOTHER_thenReturn3() {
        LocalDate birthday = LocalDate.of(2000, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.OTHER, birthday);
        assertEquals(rate, new BigDecimal("3"));
    }

    @Test
    void testGetGenderWhenDefault_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LocalDate birthday = LocalDate.of(2000, 1, 1);

                    scoringService.getGender(Gender.INCORRECT_GENDER, birthday);
                }
        );
        assertEquals("Incorrect gender", exception.getMessage());
    }

    @Test
    void testGetBaseRateAndInsurance() throws IOException {
        List<BigDecimal> file = scoringService.getBaseRateAndInsurance();

        assertEquals(new BigDecimal("15"), file.get(0));
        assertEquals(new BigDecimal("100000"), file.get(1));
    }

    @Test
    void testTotalAmountByServicesWithInsurance() throws IOException {
        BigDecimal amount = new BigDecimal("15000");
        BigDecimal result = scoringService.totalAmountByServices(amount, true);

        assertEquals(amount.add(new BigDecimal("100000")), result);
    }

    @Test
    void testTotalAmountByServicesWithoutInsurance() throws IOException {
        BigDecimal amount = new BigDecimal("15000");
        BigDecimal result = scoringService.totalAmountByServices(amount, false);

        assertEquals(amount, result);
    }

    @Test
    void testCalculateRateWhenFalseIAndFalseS() throws IOException {
        BigDecimal result = scoringService.calculateRate(false, false);
        BigDecimal rate = new BigDecimal("15");

        assertEquals(rate, result);
    }

    @Test
    void testCalculateRateWhenFalseIAndTrueS() throws IOException {
        BigDecimal result = scoringService.calculateRate(false, true);
        BigDecimal rate = new BigDecimal("14");

        assertEquals(rate, result);
    }

    @Test
    void testCalculateRateWhenTrueIAndFalseS() throws IOException {
        BigDecimal result = scoringService.calculateRate(true, false);
        BigDecimal rate = new BigDecimal("12");

        assertEquals(rate, result);
    }

    @Test
    void testCalculateRateWhenTrueIAndTrueS() throws IOException {
        BigDecimal result = scoringService.calculateRate(true, true);
        BigDecimal rate = new BigDecimal("11");

        assertEquals(rate, result);
    }

    @Test
    void testScoringRate() {
        //уже проверено, что выдается верный результат, поэтому просто тестик на корректность
        BigDecimal rate = new BigDecimal("11");

        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED, //+1
                "123123134", BigDecimal.valueOf(5000),
                Position.MIDDLE_MANAGER, 20, 3); // -2

        BigDecimal amount = new BigDecimal("100000");
        LocalDate birthday = LocalDate.of(2000, 1, 1);

        scoringDataDTO.setAmount(amount);
        scoringDataDTO.setEmployment(employmentDTO);
        scoringDataDTO.setBirthdate(birthday);
        scoringDataDTO.setMaritalStatus(MaritalStatus.MARRIED); // -3
        scoringDataDTO.setGender(Gender.MAN); // 0
        scoringDataDTO.setDependentAmount(1); //0

        BigDecimal result = scoringService.scoringRate(rate, scoringDataDTO);

        assertEquals(new BigDecimal("7"), result);
    }

    @Test
    void testGetAnnuityPayment() {
        BigDecimal rate = new BigDecimal("11");
        BigDecimal amount = new BigDecimal("10000");
        Integer term = 12;

        BigDecimal result = scoringService.getAnnuityPayment(rate, amount, term);

        assertEquals(new BigDecimal("883.82"), result);
    }

    @Test
    void testGetPSK() {
        Integer term = 12;
        BigDecimal monthlyPayment = new BigDecimal("883.82");
        BigDecimal amount = new BigDecimal("10000");

        BigDecimal result = scoringService.getPSK(term, monthlyPayment, amount);

        assertEquals(new BigDecimal("6.06"), result);
    }

    @Test
    void testCreateListPayment() {
        //это всё сравнивалось с каким-то калькулятором из интернета
        //(акцент теста на этом, поэтому смысла переносить сюда свои расчеты не вижу)
        //для этого теста либо ставить фиксированную дату в createListPayment
        //либо менять каждый месяц расчеты в соответствии с калькулятором из интернета)
        BigDecimal monthlyPayment = new BigDecimal("1051.11");
        BigDecimal rate = new BigDecimal("11");
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataDTO.setTerm(10);
        scoringDataDTO.setAmount(new BigDecimal("10000"));

        List<PaymentScheduleElement> paymentScheduleElements = scoringService.createListPayment(monthlyPayment,
                scoringDataDTO, rate);

        assertEquals(new BigDecimal("984.83"), paymentScheduleElements.get(4).getDebtPayment());
        assertEquals(new BigDecimal("38.28"), paymentScheduleElements.get(7).getInterestPayment());
        assertEquals(new BigDecimal("7094.52"), paymentScheduleElements.get(3).getRemainingDebt());
        assertEquals(new BigDecimal("1050.55"), paymentScheduleElements.get(10).getTotalPayment());
    }

    @Test
    void testCheckScoringDataDTO() {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED,
                "123123134", BigDecimal.valueOf(5000),
                Position.MIDDLE_MANAGER, 20, 3);
        BigDecimal insurance = new BigDecimal("0");
        BigDecimal amount = new BigDecimal("100000");
        //хотя, наверное, для общего случая следовало бы написать что-то вроде текущий год - 20 лет
        LocalDate birthday = LocalDate.of(2000, 1, 1);

        scoringDataDTO.setAmount(amount);
        scoringDataDTO.setEmployment(employmentDTO);
        scoringDataDTO.setBirthdate(birthday);

        Boolean check = scoringService.checkScoringDataDTO(scoringDataDTO, insurance);

        assertTrue(check);
    }

    @Test
    void testWhenEmploymentStatusUNEMPLOYED_whenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
                    EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.UNEMPLOYED,
                            "123123134", BigDecimal.valueOf(50000),
                            Position.MIDDLE_MANAGER, 20, 20);
                    BigDecimal insurance = new BigDecimal("0");
                    scoringDataDTO.setEmployment(employmentDTO);

                    scoringService.checkScoringDataDTO(scoringDataDTO, insurance);
                }
        );
        assertEquals("Unsuitable candidate, Employment Status", exception.getMessage());
    }

    @Test
    void testWhenWorkExperienceTotalLess12_whenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
                    EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED,
                            "123123134", BigDecimal.valueOf(50000),
                            Position.MIDDLE_MANAGER, 11, 20);
                    BigDecimal insurance = new BigDecimal("0");
                    scoringDataDTO.setEmployment(employmentDTO);

                    scoringService.checkScoringDataDTO(scoringDataDTO, insurance);
                }
        );
        assertEquals("Unsuitable candidate, Work Experience/Current Total", exception.getMessage());
    }

    @Test
    void testWhenWorkExperienceCurrentLess3_whenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
                    EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED,
                            "123123134", BigDecimal.valueOf(50000),
                            Position.MIDDLE_MANAGER, 20, 2);
                    BigDecimal insurance = new BigDecimal("0");
                    scoringDataDTO.setEmployment(employmentDTO);

                    scoringService.checkScoringDataDTO(scoringDataDTO, insurance);
                }
        );
        assertEquals("Unsuitable candidate, Work Experience/Current Total", exception.getMessage());
    }

    @Test
    void testWhenDiffCreditAndSalaryMore20Salary_whenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
                    EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED,
                            "123123134", BigDecimal.valueOf(4999),
                            Position.MIDDLE_MANAGER, 20, 3);
                    BigDecimal insurance = new BigDecimal("0");
                    BigDecimal amount = new BigDecimal("100000");

                    scoringDataDTO.setAmount(amount);
                    scoringDataDTO.setEmployment(employmentDTO);

                    scoringService.checkScoringDataDTO(scoringDataDTO, insurance);
                }
        );
        assertEquals("Unsuitable candidate, low Salary", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {2015, 1962})
    void testWhenCandidateIsUnder20orOver60_whenThrow(int years) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
                    EmploymentDTO employmentDTO = new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED,
                            "123123134", BigDecimal.valueOf(5000),
                            Position.MIDDLE_MANAGER, 20, 3);
                    BigDecimal insurance = new BigDecimal("0");
                    BigDecimal amount = new BigDecimal("100000");
                    LocalDate birthday = LocalDate.of(years, 1, 1);

                    scoringDataDTO.setAmount(amount);
                    scoringDataDTO.setEmployment(employmentDTO);
                    scoringDataDTO.setBirthdate(birthday);

                    scoringService.checkScoringDataDTO(scoringDataDTO, insurance);
                }
        );
        assertEquals("Unsuitable candidate, Age", exception.getMessage());
    }
}