package com.deal.service.credit.Impl;

import com.deal.dto.CreditDTO;
import com.deal.dto.FinishRegistrationRequestDTO;
import com.deal.dto.LoanOfferDTO;
import com.deal.dto.ScoringDataDTO;
import com.deal.enums.CreditStatus;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.model.Credit;
import com.deal.model.Passport;
import com.deal.repository.CreditRepository;
import com.deal.service.credit.CreditService;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public void addCreditToDB(Credit credit) {
        creditRepository.save(credit);
    }

    @Override
    public ScoringDataDTO createScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Client client,
                                            Application application) {

        Jsonb jsonb = JsonbBuilder.create();

        Passport passport = jsonb.fromJson(client.getPassport(), Passport.class);

        LoanOfferDTO loanOfferDTO = jsonb.fromJson(application.getAppliedOffer(), LoanOfferDTO.class);

        return ScoringDataDTO.builder()
                .account(finishRegistrationRequestDTO.getAccount())
                .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .employment(finishRegistrationRequestDTO.getEmployment())
                .gender(finishRegistrationRequestDTO.getGender())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .birthdate(client.getBirthday())
                .passportSeries(passport.getSeries())
                .passportNumber(passport.getNumber())
                .passportIssueBranch(passport.getIssueBranch())
                .passportIssueDate(passport.getIssueDate())
                .amount(loanOfferDTO.getRequestedAmount())
                .term(loanOfferDTO.getTerm())
                .isInsuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDTO.getIsSalaryClient())
                .build();
    }

    @Override
    public Credit createCredit(CreditDTO creditDTO) {
        Jsonb jsonb = JsonbBuilder.create();
        String paymentScheduleJsonb = jsonb.toJson(creditDTO.getPaymentSchedule());
        return Credit.builder()
                .amount(creditDTO.getAmount())
                .term(creditDTO.getTerm())
                .monthlyPayment(creditDTO.getMonthlyPayment())
                .rate(creditDTO.getRate())
                .psk(creditDTO.getPsk())
                .paymentSchedule(paymentScheduleJsonb)
                .insuranceEnable(creditDTO.getIsInsuranceEnabled())
                .salaryClient(creditDTO.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATE)
                .build();
    }

    @Override
    public Credit getCreditById(Long id) {
        return creditRepository.findById(id).get();
    }
}
