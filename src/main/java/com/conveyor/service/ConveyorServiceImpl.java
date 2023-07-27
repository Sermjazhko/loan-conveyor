package com.conveyor.service;

import com.conveyor.dto.*;
import com.conveyor.scoring.*;
import com.conveyor.validation.DataValidation;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.Math.*;

@Service
public class ConveyorServiceImpl implements ConveyorService {

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO)
            throws IOException {

        List<LoanOfferDTO> loanOfferDTOS = new ArrayList<>();

        /*  try {*/
        Double baseRate = getBaseRateAndInsurance().get(0);
        Double insurance = getBaseRateAndInsurance().get(1);

        Long id = 0L; //чисто в теории, наверное, id может потом из бд быть известно,
        // сейчас немного непонятно, как формируется, поэтому очень нубно

        Boolean isInsuranceEnabled = false, isSalaryClient = false;
        Double newRate;
        for (int i = 0; i < 3; i++) {
            newRate = rateChange(baseRate, isInsuranceEnabled, isSalaryClient);

            loanOfferDTOS.add(createOffer(newRate, loanApplicationRequestDTO.getAmount(),
                    loanApplicationRequestDTO.getTerm(), id, isInsuranceEnabled, isSalaryClient, insurance));
            id += 1;
            isInsuranceEnabled = isSalaryClient;
            isSalaryClient = !isSalaryClient;
        }
        newRate = rateChange(baseRate, true, true);

        loanOfferDTOS.add(createOffer(newRate, loanApplicationRequestDTO.getAmount(),
                loanApplicationRequestDTO.getTerm(), id, true, true, insurance));
        /*} catch (IOException e) {
            throw new IOException("File not found or request for file is invalid");
        }*/
        return loanOfferDTOS;
    }

    @Override
    public CreditDTO getCalculation(ScoringDataDTO scoringDataDTO) throws IOException {

        Double baseRate = getBaseRateAndInsurance().get(0);
        Double insurance = getBaseRateAndInsurance().get(1);
        Double requestedAmount = scoringDataDTO.getAmount().doubleValue();


        if (scoringDataDTO.getIsInsuranceEnabled()) {
            requestedAmount += insurance;
            insurance = 0.0;
        }
        if (!DataValidation.checkScoringDataDTO(scoringDataDTO, insurance)) {
            throw new IllegalArgumentException("Unsuitable candidate");
        }

        Double rateNew = rateChange(baseRate, scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient());
        System.out.println("rate= " + rateNew);
        Double rate = scoringRate(rateNew, scoringDataDTO);
        System.out.println("rate= " + rate);
        Double monthlyPayment = getAnnuityPayment(rate, requestedAmount, scoringDataDTO.getTerm());
        //TODO тоже вынести отдельно
        //упрощенная версия пск, ибо зачем сложная
        Integer term = scoringDataDTO.getTerm();
        Double psk = 1200 * ((term * monthlyPayment) / requestedAmount - 1) / term;

        List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>();
        LocalDate date = LocalDate.now();
        Double interestPayment = 0.0, debtPayment = 0.0, remainingDebt = monthlyPayment * term;
        //TODO вынести в отдельную хуету
        for (Integer i = 0; i < term; ++i) {
            paymentScheduleElements.add(new PaymentScheduleElement(i, date, BigDecimal.valueOf(monthlyPayment),
                    BigDecimal.valueOf(interestPayment), BigDecimal.valueOf(debtPayment),
                    BigDecimal.valueOf(remainingDebt)));
            date = date.plusMonths(1);
            interestPayment = 0.01 * remainingDebt * term / 12;
            debtPayment = monthlyPayment - interestPayment;
            remainingDebt = remainingDebt - monthlyPayment;
            System.out.println(i + " " + remainingDebt);
        }
        CreditDTO creditDTO = new CreditDTO(BigDecimal.valueOf(requestedAmount), term,
                BigDecimal.valueOf(monthlyPayment), BigDecimal.valueOf(rate), BigDecimal.valueOf(psk),
                scoringDataDTO.getIsInsuranceEnabled(), scoringDataDTO.getIsSalaryClient(), paymentScheduleElements);
        return creditDTO;
    }

    private Double rateChange(Double rate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        Double rateNew = rate;
        if (isInsuranceEnabled) {
            rateNew -= 3;
        }
        if (isSalaryClient) {
            rateNew -= 1;
        }
        return rateNew;
    }

    private LoanOfferDTO createOffer(Double rate, BigDecimal amount, Integer term, Long id,
                                     Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                     Double insurance) {

        Double requestedAmount = amount.doubleValue();

        if (isInsuranceEnabled) {
            requestedAmount += insurance;
        }

        Double annuityPayment = getAnnuityPayment(rate, requestedAmount, term);
        BigDecimal monthlyPayment = BigDecimal.valueOf(Math.round(annuityPayment));
        BigDecimal totalAmount = BigDecimal.valueOf(Math.round(requestedAmount));

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(id, BigDecimal.valueOf(requestedAmount),
                totalAmount, term, monthlyPayment, BigDecimal.valueOf(rate),
                isInsuranceEnabled, isSalaryClient);

        return loanOfferDTO;
    }

    private Double getAnnuityPayment(Double rate, Double requestedAmount, Integer term) {

        Double interestRate = rate * 0.01 / 12;
        return requestedAmount * (interestRate + interestRate / (pow(1 + interestRate, term) - 1));
    }

    public List<Double> getBaseRateAndInsurance() throws IOException {
        //0 - base rate, 1 - insurance
        FileInputStream file;
        Properties properties = new Properties();
        try {
            file = new FileInputStream("src/main/resources/config.properties");
            properties.load(file);
            Double baseRate = Double.parseDouble(properties.getProperty("base.rate"));
            Double insurance = Double.parseDouble(properties.getProperty("insurance"));
            file.close();
            return new ArrayList<Double>() {
                {
                    add(baseRate);
                    add(insurance);
                }
            };
        } catch (IOException e) {
            throw new IOException("File not found or request for file is invalid");
        }
    }

    private Double scoringRate(Double rate, ScoringDataDTO scoringDataDTO) {

        rate += ScoringRulesRate.getEmploymentStatus((EmploymentStatus) scoringDataDTO.getEmployment().getEmploymentStatus());
        rate += ScoringRulesRate.getPosition((Position) scoringDataDTO.getEmployment().getPosition());
        rate += ScoringRulesRate.getMaritalStatus((MaritalStatus) scoringDataDTO.getMaritalStatus());
        rate += ScoringRulesRate.getDependentAmount(scoringDataDTO.getDependentAmount());
        rate += ScoringRulesRate.getGender((Gender) scoringDataDTO.getGender(), scoringDataDTO.getBirthdate());
        return rate;
    }
}
