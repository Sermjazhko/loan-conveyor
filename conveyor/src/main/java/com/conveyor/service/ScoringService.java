package com.conveyor.service;

import com.conveyor.dto.PaymentScheduleElement;
import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.scoring.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static java.lang.Math.pow;

@Service
public class ScoringService {
    private static Logger log = Logger.getLogger(ScoringService.class.getName());

    public BigDecimal getEmploymentStatus(EmploymentStatus employmentStatus)  throws IllegalArgumentException {

        BigDecimal rate;

        switch (employmentStatus) {
            case SELF_EMPLOYED:
                rate = BigDecimal.valueOf(1);
                break;
            case BUSINESS_OWNER:
                rate = BigDecimal.valueOf(3);
                break;
            default:
                throw new IllegalArgumentException("Incorrect employment status");
        }

        return rate;
    }

    public BigDecimal getPosition(Position position)  throws IllegalArgumentException {

        BigDecimal rate;

        switch (position) {
            case WORKER:
            case OWNER:
                rate = BigDecimal.valueOf(0);
                break;
            case MIDDLE_MANAGER:
                rate = BigDecimal.valueOf(-2);
                break;
            case TOP_MANAGER:
                rate = BigDecimal.valueOf(-4);
                break;
            default:
                throw new IllegalArgumentException("Incorrect position");
        }

        return rate;
    }

    public BigDecimal getMaritalStatus(MaritalStatus maritalStatus)  throws IllegalArgumentException{

        BigDecimal rate;

        switch (maritalStatus) {
            case MARRIED:
                rate = BigDecimal.valueOf(-3);
                break;
            case DIVORCED:
                rate = BigDecimal.valueOf(-1);
                break;
            case SINGLE:
            case WIDOW_WIDOWER:
                rate = BigDecimal.valueOf(0);
                break;
            default:
                throw new IllegalArgumentException("Incorrect marital status");
        }

        return rate;
    }

    public BigDecimal getDependentAmount(Integer dependentAmount) {

        BigDecimal rate = BigDecimal.valueOf(0);

        if (dependentAmount > 1) {
            rate = rate.add(BigDecimal.valueOf(1));
        }
        return rate;
    }

    public BigDecimal getGender(Gender gender, LocalDate localDate)  throws IllegalArgumentException {

        LocalDate localDateNow = LocalDate.now();
        long years = localDate.until(localDateNow, ChronoUnit.YEARS);
        BigDecimal rate = BigDecimal.valueOf(0);

        switch (gender) {
            case FEMALE:
                if (34 < years && years < 61) {
                    rate = BigDecimal.valueOf(-3);
                }
                break;
            case MALE:
                if (29 < years && years < 56) {
                    rate = BigDecimal.valueOf(-3);
                }
                break;
            case NOT_BINARY:
                rate = BigDecimal.valueOf(3);
                break;
            default:
                throw new IllegalArgumentException("Incorrect gender");
        }

        return rate;
    }

    public List<BigDecimal> getBaseRateAndInsurance() throws IOException {
        //0 - base rate, 1 - insurance
        FileInputStream file;
        Properties properties = new Properties();
        try {
            file = new FileInputStream("/conveyor/src/main/resources/config.properties");
            properties.load(file);
            BigDecimal baseRate = new BigDecimal(properties.getProperty("base.rate"));
            BigDecimal insurance = new BigDecimal(properties.getProperty("insurance"));
            file.close();

            log.info("Successful reading of data from file properties");
            return new ArrayList<BigDecimal>() {
                {
                    add(baseRate);
                    add(insurance);
                }
            };
        } catch (IOException e) {
            log.info("Unsuccessful reading of data from file properties");
            throw new IOException("File not found or request for file is invalid");
        }
    }

    public BigDecimal totalAmountByServices(BigDecimal amount, Boolean isInsuranceEnabled) throws IOException {

        BigDecimal insurance = getBaseRateAndInsurance().get(1);
        if (isInsuranceEnabled) {
            amount = amount.add(insurance);
        }

        return amount;
    }

    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) throws IOException {

        BigDecimal rate = getBaseRateAndInsurance().get(0);

        if (isInsuranceEnabled) {
            rate = rate.add(BigDecimal.valueOf(-3));
        }
        if (isSalaryClient) {
            rate = rate.add(BigDecimal.valueOf(-1));
        }

        return rate;
    }

    public BigDecimal scoringRate(BigDecimal rate, ScoringDataDTO scoringDataDTO) {

        rate = rate.add(getEmploymentStatus((EmploymentStatus) scoringDataDTO.getEmployment().getEmploymentStatus()));
        rate = rate.add(getPosition((Position) scoringDataDTO.getEmployment().getPosition()));
        rate = rate.add(getMaritalStatus((MaritalStatus) scoringDataDTO.getMaritalStatus()));
        rate = rate.add(getDependentAmount(scoringDataDTO.getDependentAmount()));
        rate = rate.add(getGender((Gender) scoringDataDTO.getGender(), scoringDataDTO.getBirthdate()));

        return rate;
    }

    public BigDecimal getAnnuityPayment(BigDecimal rate, BigDecimal requestedAmount, Integer term) {

        Double interestRate = rate.doubleValue() * 0.01 / 12;
        Double payment = requestedAmount.doubleValue() * (interestRate + interestRate / (pow(1 + interestRate, term) - 1));

        BigDecimal result = BigDecimal.valueOf(payment).setScale(2, RoundingMode.HALF_UP);

        return result;
    }

    public BigDecimal getPSK(Integer term, BigDecimal monthlyPayment, BigDecimal requestedAmount) {

        //упрощенная версия пск
        Double psk = 1200 * ((term.doubleValue() * monthlyPayment.doubleValue()) /
                requestedAmount.doubleValue() - 1) / term;

        BigDecimal result = BigDecimal.valueOf(psk).setScale(2, RoundingMode.HALF_UP);

        return result;
    }

    public List<PaymentScheduleElement> createListPayment(BigDecimal monthlyPayment, ScoringDataDTO scoringDataDTO,
                                                          BigDecimal rate) throws IOException {

        List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>();

        Double insurance = 0.0;

        if (scoringDataDTO.getIsInsuranceEnabled()) {
            insurance = getBaseRateAndInsurance().get(1).doubleValue();
        }

        LocalDate date = LocalDate.now();
        Integer term = scoringDataDTO.getTerm();
        Double monthlyPaymentDoub = monthlyPayment.doubleValue(), rateDoub = rate.doubleValue();

        Double interestPayment = 0.0, debtPayment = 0.0, remainingDebt = scoringDataDTO.getAmount().doubleValue() +
                insurance;

        for (Integer i = 0; i <= term; ++i) {
            if (i == term) {
                monthlyPayment = monthlyPayment.add(
                        new BigDecimal(remainingDebt).setScale(2, RoundingMode.HALF_UP));
                remainingDebt = 0.0;
            }
            paymentScheduleElements.add(new PaymentScheduleElement(i, date, monthlyPayment,
                    BigDecimal.valueOf(interestPayment).setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.valueOf(debtPayment).setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.valueOf(remainingDebt).setScale(2, RoundingMode.HALF_UP)));
            interestPayment = remainingDebt * (0.01 * rateDoub / 12);
            date = date.plusMonths(1);
            debtPayment = monthlyPaymentDoub - interestPayment;
            remainingDebt = remainingDebt - debtPayment;
        }

        return paymentScheduleElements;
    }

    public boolean checkScoringDataDTO(ScoringDataDTO scoringDataDTO, BigDecimal insurance)  throws IllegalArgumentException{
        //если страховки нет, передаем 0
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new IllegalArgumentException("Unsuitable candidate, Employment Status");
        }

        if ((scoringDataDTO.getEmployment().getWorkExperienceTotal() < 12) ||
                (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < 3)) {
            throw new IllegalArgumentException("Unsuitable candidate, Work Experience/Current Total");
        }

        Double diffCreditAndSalary = scoringDataDTO.getEmployment().getSalary().doubleValue() * 20
                - scoringDataDTO.getAmount().doubleValue() - insurance.doubleValue();
        if (diffCreditAndSalary < 0) {
            throw new IllegalArgumentException("Unsuitable candidate, low Salary");
        }

        LocalDate localDateNow = LocalDate.now();
        long years = scoringDataDTO.getBirthdate().until(localDateNow, ChronoUnit.YEARS);

        if (years < 20 || years > 60) {
            throw new IllegalArgumentException("Unsuitable candidate, Age");
        }
        return true;
    }

}
