package com.conveyor.service;

import com.conveyor.dto.EmploymentDTO;
import com.conveyor.dto.PaymentScheduleElement;
import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.scoring.EmploymentStatus;
import com.conveyor.scoring.Gender;
import com.conveyor.scoring.MaritalStatus;
import com.conveyor.scoring.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {


    @InjectMocks
    private ScoringService scoringService;

    @Test
    void getEmploymentStatusWhenSELF_EMPLOYED_thenReturn1() {
        BigDecimal rate = scoringService.getEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        assertEquals(rate, new BigDecimal("1"));
    }

    @Test
    void getEmploymentStatusWhenBUSINESS_thenReturn1() {
        BigDecimal rate = scoringService.getEmploymentStatus(EmploymentStatus.BUSINESS);
        assertEquals(rate, new BigDecimal("3"));
    }

    @Test
    void getEmploymentStatusWhenOther_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    scoringService.getEmploymentStatus(EmploymentStatus.NOT);
                }
        );
        assertEquals("Not employment status", exception.getMessage());
    }


    @Test
    void getPositionWhenMANAGER_thenReturn0() {
        BigDecimal rate = scoringService.getPosition(Position.MANAGER);
        assertEquals(rate, new BigDecimal("0"));
    }

    @Test
    void getPositionWhenMIDDLE_MANAGER_thenReturnMin2() {
        BigDecimal rate = scoringService.getPosition(Position.MIDDLE_MANAGER);
        assertEquals(rate, new BigDecimal("-2"));
    }

    @Test
    void getPositionWhenTOP_MANAGER_thenReturnMin4() {
        BigDecimal rate = scoringService.getPosition(Position.TOP_MANAGER);
        assertEquals(rate, new BigDecimal("-4"));
    }

    @Test
    void getPositionWhenOther_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    scoringService.getPosition(Position.NOT);
                }
        );
        assertEquals("Not position", exception.getMessage());
    }

    @Test
    void getMaritalStatusWhenMARRIED_thenReturnMin3() {
        BigDecimal rate = scoringService.getMaritalStatus(MaritalStatus.MARRIED);
        assertEquals(rate, new BigDecimal("-3"));
    }

    @Test
    void getMaritalStatusWhenNOT_MARRIED_thenReturnMin1() {
        BigDecimal rate = scoringService.getMaritalStatus(MaritalStatus.NOT_MARRIED);
        assertEquals(rate, new BigDecimal("-1"));
    }

    @Test
    void getMaritalStatusWhenOther_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    scoringService.getMaritalStatus(MaritalStatus.NOT);
                }
        );
        assertEquals("Not marital status", exception.getMessage());
    }

    @Test
    void getDependentAmountWhenMore1_then1() {
        Integer dependentAmount = 3;
        BigDecimal rate = scoringService.getDependentAmount(dependentAmount);
        assertEquals(new BigDecimal("1"), rate);
    }

    @Test
    void getDependentAmountWhenLess1_then0() {
        Integer dependentAmount = 1;
        BigDecimal rate = scoringService.getDependentAmount(dependentAmount);
        assertEquals(new BigDecimal("0"), rate);
    }


    @ParameterizedTest
    @ValueSource(ints = {1988, 1980, 1970, 1963})
    void getGenderWhenSuitableWOMAN_thenReturnMin3(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.WOMAN, birthday);
        assertEquals(rate, new BigDecimal("-3"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1990, 1989, 1962, 1950})
    void getGenderWhenUnsuitableWOMAN_thenReturn0(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.WOMAN, birthday);
        assertEquals(rate, new BigDecimal("0"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1993, 1980, 1970, 1968})
    void getGenderWhenSuitableMAN_thenReturnMin3(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.MAN, birthday);
        assertEquals(rate, new BigDecimal("-3"));
    }

    @ParameterizedTest
    @ValueSource(ints = {2000, 1994, 1967, 1950})
    void getGenderWhenUnsuitableMAN_thenReturn0(int years) {
        LocalDate birthday = LocalDate.of(years, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.MAN, birthday);
        assertEquals(rate, new BigDecimal("0"));
    }

    @Test
    void getGenderWhenOTHER_thenReturn3() {
        LocalDate birthday = LocalDate.of(2000, 1, 1);

        BigDecimal rate = scoringService.getGender(Gender.OTHER, birthday);
        assertEquals(rate, new BigDecimal("3"));
    }

    @Test
    void getGenderWhenDefault_thenThrow() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    LocalDate birthday = LocalDate.of(2000, 1, 1);

                    scoringService.getGender(Gender.NOT, birthday);
                }
        );
        assertEquals("Not gender", exception.getMessage());
    }

    @Test
    void getBaseRateAndInsurance() throws IOException {
        List<BigDecimal> file = scoringService.getBaseRateAndInsurance();

        assertEquals(new BigDecimal("15"), file.get(0));
        assertEquals(new BigDecimal("100000"), file.get(1));
    }

    @Test
    void totalAmountByServicesWithInsurance() throws IOException {
        BigDecimal amount = new BigDecimal("15000");
        BigDecimal result = scoringService.totalAmountByServices(amount, true);

        assertEquals(amount.add(new BigDecimal("100000")), result);
    }

    @Test
    void totalAmountByServicesWithoutInsurance() throws IOException {
        BigDecimal amount = new BigDecimal("15000");
        BigDecimal result = scoringService.totalAmountByServices(amount, false);

        assertEquals(amount, result);
    }

    @Test
    void calculateRateWhenFalseIAndFalseS() throws IOException {
        BigDecimal result = scoringService.calculateRate(false, false);
        BigDecimal rate = new BigDecimal("15");

        assertEquals(rate, result);
    }

    @Test
    void calculateRateWhenFalseIAndTrueS() throws IOException {
        BigDecimal result = scoringService.calculateRate(false, true);
        BigDecimal rate = new BigDecimal("14");

        assertEquals(rate, result);
    }

    @Test
    void calculateRateWhenTrueIAndFalseS() throws IOException {
        BigDecimal result = scoringService.calculateRate(true, false);
        BigDecimal rate = new BigDecimal("12");

        assertEquals(rate, result);
    }

    @Test
    void calculateRateWhenTrueIAndTrueS() throws IOException {
        BigDecimal result = scoringService.calculateRate(true, true);
        BigDecimal rate = new BigDecimal("11");

        assertEquals(rate, result);
    }

    @Test
    void scoringRate() {
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
    void getAnnuityPayment() {
        BigDecimal rate = new BigDecimal("11");
        BigDecimal amount = new BigDecimal("10000");
        Integer term = 12;

        BigDecimal result = scoringService.getAnnuityPayment(rate, amount, term);

        assertEquals(new BigDecimal("883.82"), result);
    }

    @Test
    void getPSK() {
        Integer term = 12;
        BigDecimal monthlyPayment = new BigDecimal("883.82");
        BigDecimal amount = new BigDecimal("10000");

        BigDecimal result = scoringService.getPSK(term, monthlyPayment, amount);

        assertEquals(new BigDecimal("6.06"), result);
    }

    @Test
    void createListPayment() {
        //это всё сравнивалось с каким-то калькулятором из интернета
        BigDecimal monthlyPayment = new BigDecimal("1051.11");
        BigDecimal rate = new BigDecimal("11");
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataDTO.setTerm(10);
        scoringDataDTO.setAmount(new BigDecimal("10000"));

        List<PaymentScheduleElement> paymentScheduleElements = scoringService.createListPayment(monthlyPayment,
                scoringDataDTO, rate);

        assertEquals(new BigDecimal("986.94"), paymentScheduleElements.get(4).getDebtPayment());
        assertEquals(new BigDecimal("35.85"), paymentScheduleElements.get(7).getInterestPayment());
        assertEquals(new BigDecimal("7097.27"), paymentScheduleElements.get(3).getRemainingDebt());
        assertEquals(new BigDecimal("1052.67"), paymentScheduleElements.get(10).getTotalPayment());
    }

    @Test
    void checkScoringDataDTO() {
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
    void whenEmploymentStatusUNEMPLOYED_whenThrow() {
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
    void whenWorkExperienceTotalLess12_whenThrow() {
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
    void whenWorkExperienceCurrentLess3_whenThrow() {
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
    void whenDiffCreditAndSalaryMore20Salary_whenThrow() {
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
    void whenCandidateIsUnder20orOver60_whenThrow(int years) {
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