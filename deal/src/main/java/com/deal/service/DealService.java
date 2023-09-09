package com.deal.service;

import com.deal.dto.CreditDTO;
import com.deal.dto.LoanApplicationRequestDTO;
import com.deal.dto.LoanOfferDTO;
import com.deal.dto.PaymentScheduleElement;
import com.deal.enums.ApplicationStatus;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.model.Credit;
import com.deal.model.Passport;
import com.deal.service.application.ApplicationService;
import com.deal.service.client.ClientService;
import com.deal.service.credit.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private final ApplicationService applicationService;
    private final ClientService clientService;
    private final CreditService creditService;
    private final MessageService messageService;

    public ResponseEntity<LoanApplicationRequestDTO> getLoanApplicationByApplicationId(Long applicationId) {
        Application application = applicationService.getApplicationById(applicationId);

        try (Jsonb jsonb = JsonbBuilder.create()) {
            Client client = clientService.getClientById(application.getClientId());
            LoanOfferDTO loanOfferDTO = jsonb.fromJson(application.getAppliedOffer(), LoanOfferDTO.class);
            Passport passport = jsonb.fromJson(client.getPassport(), Passport.class);

            return new ResponseEntity<>(
                    LoanApplicationRequestDTO.builder()
                            .amount(loanOfferDTO.getRequestedAmount())
                            .term(loanOfferDTO.getTerm())
                            .firstName(client.getFirstName())
                            .lastName(client.getLastName())
                            .middleName(client.getMiddleName())
                            .email(client.getEmail())
                            .birthdate(client.getBirthday())
                            .passportSeries(passport.getSeries())
                            .passportNumber(passport.getNumber())
                            .build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CreditDTO> getCreditByApplicationId(Long applicationId) {
        Credit credit = creditService.getCreditById(applicationId);

        try (Jsonb jsonb = JsonbBuilder.create()) {
            List<PaymentScheduleElement> listPayment =
                    jsonb.fromJson(credit.getPaymentSchedule(), List.class);
            log.info("Status History: " + listPayment);
            return new ResponseEntity<>(
                    CreditDTO.builder()
                            .amount(credit.getAmount())
                            .term(credit.getTerm())
                            .monthlyPayment(credit.getMonthlyPayment())
                            .rate(credit.getRate())
                            .psk(credit.getPsk())
                            .isInsuranceEnabled(credit.getInsuranceEnable())
                            .isSalaryClient(credit.getSalaryClient())
                            .paymentSchedule(listPayment)
                            .build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<LoanOfferDTO> getOfferByApplicationId(Long applicationId) {
        Application application = applicationService.getApplicationById(applicationId);

        try (Jsonb jsonb = JsonbBuilder.create()) {
            LoanOfferDTO loanOfferDTO = jsonb.fromJson(application.getAppliedOffer(), LoanOfferDTO.class);
            return new ResponseEntity<>(loanOfferDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendMessage(Long applicationId, String topicName, ApplicationStatus applicationStatus) {
        Application application = applicationService.getApplicationById(applicationId);
        Long clientId = application.getClientId();
        Client client = clientService.getClientById(clientId);
        messageService.sendMessage(applicationStatus, client.getEmail(), applicationId, topicName);
    }
}
