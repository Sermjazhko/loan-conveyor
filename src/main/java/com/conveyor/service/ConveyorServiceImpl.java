package com.conveyor.service;

import com.conveyor.dto.*;
import com.conveyor.scoring.*;
import com.conveyor.validation.DataValidation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.*;

@Service
public class ConveyorServiceImpl implements ConveyorService {

    private static Logger log = Logger.getLogger(ConveyorServiceImpl.class.getName());

    private final ScoringService service;

    public ConveyorServiceImpl(ScoringService service) {
        this.service = service;
    }


    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO)
            throws IOException {

        return List.of(
                createOffer(false, false, loanApplicationRequestDTO),
                createOffer(false, true, loanApplicationRequestDTO),
                createOffer(true, false, loanApplicationRequestDTO),
                createOffer(true, true, loanApplicationRequestDTO)
        );
    }

    private LoanOfferDTO createOffer(Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                     LoanApplicationRequestDTO loanApplicationRequestDTO) throws IOException {

        BigDecimal totalAmount = service.totalAmountByServices(loanApplicationRequestDTO.getAmount(),
                isInsuranceEnabled);

        log.info("Total amount = " + totalAmount);
        BigDecimal rate = service.calculateRate(isInsuranceEnabled, isSalaryClient);

        log.info("New rate = " + rate);
        Long id = 0L; //чисто в теории, наверное, id может потом из бд быть известно,
        // сейчас немного непонятно, как формируется, поэтому очень нубно

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(
                id,
                loanApplicationRequestDTO.getAmount(),
                totalAmount,
                loanApplicationRequestDTO.getTerm(),
                getAnnuityPayment(rate, totalAmount, loanApplicationRequestDTO.getTerm()),
                rate,
                isInsuranceEnabled,
                isSalaryClient);

        return loanOfferDTO;

    }

    @Override
    public CreditDTO getCalculation(ScoringDataDTO scoringDataDTO) throws IOException {

        //скоринг данных
        BigDecimal insurance = service.getBaseRateAndInsurance().get(1);
        BigDecimal requestedAmount = scoringDataDTO.getAmount();
        log.info("Insurance = " + insurance);

        if (scoringDataDTO.getIsInsuranceEnabled()) {
            requestedAmount = requestedAmount.add(insurance);
        }

        if (!DataValidation.checkScoringDataDTO(scoringDataDTO, insurance)) {
            throw new IllegalArgumentException("Unsuitable candidate");
        }

        //высчитывание ставки

        BigDecimal rate = service.scoringRate(service.calculateRate(scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient()), scoringDataDTO);

        log.info("New rate = " + rate);

        //подсчет пск, размер ежемесячного платежа
        BigDecimal monthlyPayment = getAnnuityPayment(rate, requestedAmount, scoringDataDTO.getTerm());

        log.info("Monthly payment = " + monthlyPayment);
        Integer term = scoringDataDTO.getTerm();
        BigDecimal psk = getPSK(scoringDataDTO, monthlyPayment, requestedAmount);

        log.info("psk = " + psk);

        // график ежемесячных платежей
        List<PaymentScheduleElement> paymentScheduleElements = createListPayment(monthlyPayment, scoringDataDTO);

        return new CreditDTO(requestedAmount,
                term,
                monthlyPayment,
                rate,
                psk,
                scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient(),
                paymentScheduleElements);
    }

    private BigDecimal getAnnuityPayment(BigDecimal rate, BigDecimal requestedAmount, Integer term) {

        Double interestRate = rate.doubleValue() * 0.01 / 12;
        Double result = requestedAmount.doubleValue() * (interestRate + interestRate / (pow(1 + interestRate, term) - 1));

        return BigDecimal.valueOf(result);
    }

    private BigDecimal getPSK(ScoringDataDTO scoringDataDTO, BigDecimal monthlyPayment, BigDecimal requestedAmount) {

        //упрощенная версия пск
        Integer term = scoringDataDTO.getTerm();
        Double psk = 1200 * ((term.doubleValue() * monthlyPayment.doubleValue()) /
                requestedAmount.doubleValue() - 1) / term;

        return BigDecimal.valueOf(psk);
    }

    private List<PaymentScheduleElement> createListPayment(BigDecimal monthlyPayment, ScoringDataDTO scoringDataDTO) {

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
