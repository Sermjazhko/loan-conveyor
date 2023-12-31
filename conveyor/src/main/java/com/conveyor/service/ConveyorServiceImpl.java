package com.conveyor.service;

import com.conveyor.dto.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ConveyorServiceImpl implements ConveyorService {

    private static Logger log = Logger.getLogger(ConveyorServiceImpl.class.getName());

    private static Long idApp = 0L;

    private final ScoringService scoringService;

    public ConveyorServiceImpl(ScoringService service) {
        this.scoringService = service;
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

        BigDecimal totalAmount = scoringService.totalAmountByServices(loanApplicationRequestDTO.getAmount(),
                isInsuranceEnabled);

        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);

        BigDecimal monthlyPayment = scoringService.getAnnuityPayment(rate, totalAmount, loanApplicationRequestDTO.getTerm());

        Long id = idApp++; //чисто в теории, наверное, id может потом из бд быть известно,
        // сейчас немного непонятно, как формируется, поэтому очень нубно

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(id)
                .requestedAmount(loanApplicationRequestDTO.getAmount())
                .totalAmount(totalAmount)
                .term(loanApplicationRequestDTO.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("Successful creation of loan offer, total amount = " + totalAmount + " new rate = " + rate);

        return loanOfferDTO;
    }

    @Override
    public CreditDTO getCalculation(ScoringDataDTO scoringDataDTO) throws IOException, IllegalArgumentException {

        //скоринг данных
        BigDecimal insurance = scoringService.getBaseRateAndInsurance().get(1);
        BigDecimal requestedAmount = scoringDataDTO.getAmount();
        log.info("Insurance = " + insurance);

        if (scoringDataDTO.getIsInsuranceEnabled()) {
            requestedAmount = requestedAmount.add(insurance);
        } else {
            insurance = new BigDecimal("0");
        }

        scoringService.checkScoringDataDTO(scoringDataDTO, insurance);

        //высчитывание ставки
        BigDecimal rate = scoringService.scoringRate(scoringService.calculateRate(scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient()), scoringDataDTO);

        log.info("New rate = " + rate);

        //подсчет пск, размер ежемесячного платежа
        BigDecimal monthlyPayment = scoringService.getAnnuityPayment(rate, requestedAmount, scoringDataDTO.getTerm());

        log.info("Monthly payment = " + monthlyPayment);
        Integer term = scoringDataDTO.getTerm();
        BigDecimal psk = scoringService.getPSK(term, monthlyPayment, requestedAmount);

        log.info("psk = " + psk);

        // график ежемесячных платежей
        List<PaymentScheduleElement> paymentScheduleElements =
                scoringService.createListPayment(monthlyPayment, scoringDataDTO, rate);

        log.info("payment schedule = " + paymentScheduleElements);

        CreditDTO creditDTO = CreditDTO.builder()
                .amount(requestedAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(paymentScheduleElements)
                .build();

        log.info("Credit: " + creditDTO);

        return creditDTO;
    }
}
