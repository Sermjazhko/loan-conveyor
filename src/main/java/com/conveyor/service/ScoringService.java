package com.conveyor.service;

import com.conveyor.dto.PaymentScheduleElement;
import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.scoring.EmploymentStatus;
import com.conveyor.scoring.Gender;
import com.conveyor.scoring.MaritalStatus;
import com.conveyor.scoring.Position;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    public BigDecimal getEmploymentStatus(EmploymentStatus employmentStatus) {

        BigDecimal rate;

        switch (employmentStatus){
            case SELF_EMPLOYED:
                rate = BigDecimal.valueOf(1);
                break;
            case BUSINESS:
                rate = BigDecimal.valueOf(3);
                break;
            default:
                throw new IllegalArgumentException("Not employment status");
        }

        return rate;
    }

    public BigDecimal getPosition(Position position) {

        BigDecimal rate;

        switch (position){
            case MANAGER:
                rate = BigDecimal.valueOf(0);
                break;
            case MIDDLE_MANAGER:
                rate = BigDecimal.valueOf(-2);
                break;
            case TOP_MANAGER:
                rate = BigDecimal.valueOf(-4);
                break;
            default:
                throw new IllegalArgumentException("Not position");
        }

        return rate;
    }

    public BigDecimal getMaritalStatus(MaritalStatus maritalStatus) {

        BigDecimal rate;

        switch (maritalStatus){
            case MARRIED:
                rate = BigDecimal.valueOf(-3);
                break;
            case NOT_MARRIED:
                rate = BigDecimal.valueOf(-1);
                break;
            default:
                throw new IllegalArgumentException("Not marital status");
        }

        return rate;
    }

    public BigDecimal getDependentAmount(Integer dependentAmount) {

        BigDecimal rate = BigDecimal.valueOf(0);;
        if(dependentAmount > 1) {
            rate = rate.add(BigDecimal.valueOf(1));
        }
        return rate;
    }

    public BigDecimal getGender(Gender gender, LocalDate localDate) {

        LocalDate localDateNow = LocalDate.now();
        long years = localDate.until(localDateNow, ChronoUnit.YEARS);
        BigDecimal rate = BigDecimal.valueOf(0);;

        switch (gender){
            case WOMAN:
                if (34 < years && years < 61) {
                    rate = BigDecimal.valueOf(-3);
                }
                break;
            case MAN:
                if (29 < years && years < 56) {
                    rate = BigDecimal.valueOf(-3);
                }
                break;
            case OTHER:
                rate = BigDecimal.valueOf(3);
                break;
            default:
                throw new IllegalArgumentException("Not employment status");
        }

        return rate;
    }

    public List<BigDecimal> getBaseRateAndInsurance() throws IOException {
        //0 - base rate, 1 - insurance
        FileInputStream file;
        Properties properties = new Properties();

        try {
            file = new FileInputStream("src/main/resources/config.properties");
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
        Double result = requestedAmount.doubleValue() * (interestRate + interestRate / (pow(1 + interestRate, term) - 1));

        return BigDecimal.valueOf(result);
    }

    public BigDecimal getPSK(ScoringDataDTO scoringDataDTO, BigDecimal monthlyPayment, BigDecimal requestedAmount) {

        //упрощенная версия пск
        Integer term = scoringDataDTO.getTerm();
        Double psk = 1200 * ((term.doubleValue() * monthlyPayment.doubleValue()) /
                requestedAmount.doubleValue() - 1) / term;

        return BigDecimal.valueOf(psk);
    }

    public List<PaymentScheduleElement> createListPayment(BigDecimal monthlyPayment, ScoringDataDTO scoringDataDTO) {

        List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>();

        LocalDate date = LocalDate.now();
        Integer term = scoringDataDTO.getTerm();
        Double monthlyPaymentDoub = monthlyPayment.doubleValue();

        Double interestPayment = 0.0, debtPayment = 0.0, remainingDebt = monthlyPaymentDoub * term;

        for (Integer i = 0; i < term; ++i) {
            paymentScheduleElements.add(new PaymentScheduleElement(i, date, monthlyPayment,
                    BigDecimal.valueOf(interestPayment), BigDecimal.valueOf(debtPayment),
                    BigDecimal.valueOf(remainingDebt)));
            //изменяем инфу
            date = date.plusMonths(1);
            interestPayment = 0.01 * remainingDebt * term / 12;
            debtPayment = monthlyPaymentDoub - interestPayment;
            remainingDebt = remainingDebt - monthlyPaymentDoub;
        }

        return paymentScheduleElements;
    }

}
