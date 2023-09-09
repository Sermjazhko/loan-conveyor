package com.deal.controller;

import com.deal.dto.*;
import com.deal.enums.ApplicationStatus;
import com.deal.enums.ChangeType;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.model.Credit;
import com.deal.model.StatusHistory;
import com.deal.service.DealService;
import com.deal.service.MessageService;
import com.deal.service.application.ApplicationService;
import com.deal.service.client.ClientService;
import com.deal.service.credit.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Tag(name = "Deal controller", description = "Implementation of the deal microservice")
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private static final String TOPIC_FINISH_REGISTRATION = "finish-registration";
    private static final String TOPIC_CREATE_DOCUMENT = "create-documents";
    private static final String TOPIC_SEND_DOCUMENTS = "send-documents";
    private static final String TOPIC_SEND_SES = "send-ses";
    private static final String TOPIC_CREDIT_ISSUED = "credit-issued";
    private static final String TOPIC_APPLICATION_DENIED = "application-denied";

    private final ClientService clientService;
    private final ApplicationService applicationService;
    private final CreditService creditService;
    private final MessageService messageService;
    private final DealService dealService;

    @Operation(
            summary = "Calculation of possible loan terms",
            description = "Based on the LoanApplicationRequestDTO, Client entity is created and stored in the database." +
                    "Application is created with a link to the newly created Client and stored in the database." +
                    "A POST request is sent to /conveyor/offers MS conveyor via RestTemplate. " +
                    "Each item from the list<LoanOfferDTO> is assigned the id of the created application"
    )
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> getPostOffer(@RequestBody
                                                           @Parameter(description = "Заявка на получение кредита")
                                                           LoanApplicationRequestDTO loanApplicationRequestDTO) {

        //На основе LoanApplicationRequestDTO создаётся сущность Client и сохраняется в БД.
        log.info("Loan application request: " + loanApplicationRequestDTO);

        String resultPassport = clientService.createPassport(loanApplicationRequestDTO);
        log.info("Result jsonb passport: " + resultPassport);

        Client client = clientService.createClient(loanApplicationRequestDTO, resultPassport);
        log.info("Client: " + client);

        clientService.addClientToDB(client);
        log.info("Client add!");

        //Создаётся Application со связью на только что созданный Client и сохраняется в БД.
        List<StatusHistory> list = new ArrayList<>();

        String resultHistory = applicationService.createStatusHistory(list,
                ApplicationStatus.PREPARE_DOCUMENTS, ChangeType.AUTOMATIC, new Date());
        log.info("Result jsonb list history: " + resultHistory);

        Application application = applicationService.createApplication(client.getId(), resultHistory);

        log.info("Application: " + application);

        applicationService.addApplicationToDB(application);
        log.info("Application add!");

        //Отправляется POST запрос на /conveyor/offers МС conveyor через FeignClient (здесь и далее вместо
        // FeignClient можно использовать RestTemplate). Каждому элементу из списка List<LoanOfferDTO> присваивается id созданной заявки (Application)
        String resourceUrl = "http://localhost:9090/conveyor/offers";

        RestTemplate restTemplate = new RestTemplate();

        log.info("Start POST request!");

        HttpEntity<LoanApplicationRequestDTO> request =
                new HttpEntity<LoanApplicationRequestDTO>(loanApplicationRequestDTO);

        ResponseEntity<List<LoanOfferDTO>> rateResponse =
                restTemplate.exchange(resourceUrl,
                        HttpMethod.POST, request, new ParameterizedTypeReference<List<LoanOfferDTO>>() {
                        });
        log.info("End POST request!");

        List<LoanOfferDTO> loanOfferDTOS = rateResponse.getBody();

        for (int index = 0; index < 4; ++index) {
            loanOfferDTOS.get(index).setApplicationId(application.getId());
        }

        log.info("loanOffer: " + loanOfferDTOS);

        return new ResponseEntity<>(loanOfferDTOS, HttpStatus.CREATED);

    }

    @Operation(
            summary = "Choosing one of the offers",
            description = "The application is obtained from the database by applicationId from LoanOfferDTO." +
                    "The status is updated in the application, the status history, the accepted LoanOfferDTO offer" +
                    " is set in the appliedOffer field." +
                    "The application is saved."
    )
    @PutMapping("/offer")
    public void putOffer(@RequestBody LoanOfferDTO loanOfferDTO)  {

        //По API приходит LoanOfferDTO
        // Достаётся из БД заявка(Application) по applicationId из LoanOfferDTO.
        log.info("Input data to the offer, loanOffer: " + loanOfferDTO);
        Long id = loanOfferDTO.getApplicationId();
        Application application = applicationService.getApplicationById(id);
        log.info("Id application: " + id + ". Application: " + application);
        //В заявке обновляется статус, история статусов(List<ApplicationStatusHistoryDTO>),
        // принятое предложение LoanOfferDTO устанавливается в поле appliedOffer.
        Date date = new Date();
        application = applicationService.updateApplicationStatusHistory(application, date,
                ApplicationStatus.APPROVED);

        Jsonb jsonb = JsonbBuilder.create();
        String resultOffer = jsonb.toJson(loanOfferDTO);
        application.setAppliedOffer(resultOffer);
        log.info("New application: " + application);

        //Заявка сохраняется.
        applicationService.addApplicationToDB(application);
        log.info("Application update!");

        Long clientId = application.getClientId();
        Client client = clientService.getClientById(clientId);

        messageService.sendMessage(ApplicationStatus.APPROVED, client.getEmail(), id, TOPIC_FINISH_REGISTRATION);
    }

    @Operation(
            summary = "Completion of registration + full credit calculation.",
            description = "The application is retrieved from the database by applicationId." +
                    "The ScoringDataDTO is saturated with information from the FinishRegistrationRequestDTO " +
                    "and the Client, which is stored in the Application" +
                    "A POST request is sent to the MC CC with the body of the ScoringDataDTO." +
                    "The loan body is created and added to the database."
    )
    @PutMapping("/calculate/{applicationId}")
    public void putCalculate(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                             @PathVariable(value = "applicationId") Long applicationId) {

        //По API приходит объект FinishRegistrationRequestDTO и параметр applicationId (Long).
        //Достаётся из БД заявка(Application) по applicationId.
        log.info("Input data to the calculation, Finish registration request: " + finishRegistrationRequestDTO);

        Application application = applicationService.getApplicationById(applicationId);
        log.info("Application: " + application);

        //ScoringDataDTO насыщается информацией из FinishRegistrationRequestDTO и Client, который хранится в Application
        Client client = clientService.getClientById(application.getClientId());

        Jsonb jsonb = JsonbBuilder.create();
        String strEmployment = jsonb.toJson(finishRegistrationRequestDTO.getEmployment());
        //обновляем клиента
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        client.setEmployment(strEmployment);
        client.setAccount(finishRegistrationRequestDTO.getAccount());
        log.info("client: " + client);

        clientService.addClientToDB(client);
        log.info("Client update!");

        //обновим историю статусов
        Date date = new Date();
        application = applicationService.updateApplicationStatusHistory(application, date,
                ApplicationStatus.CC_APPROVED);

        log.info("Application: " + application);

        applicationService.addApplicationToDB(application);
        log.info("Application update!");


        ScoringDataDTO scoringDataDTO = creditService.createScoringData(finishRegistrationRequestDTO,
                client, application);
        log.info("Scoring data: " + scoringDataDTO);

        try {
            String resourceUrl = "http://localhost:9090/conveyor/calculation";

            //Отправляется POST запрос к МС КК с телом ScoringDataDTO
            RestTemplate restTemplate = new RestTemplate();
            log.info("Start POST request!");

            HttpEntity<ScoringDataDTO> request = new HttpEntity<ScoringDataDTO>(scoringDataDTO);
            ResponseEntity<CreditDTO> creditResponse =
                    restTemplate.exchange(resourceUrl,
                            HttpMethod.POST, request, new ParameterizedTypeReference<CreditDTO>() {
                            });
            log.info("End POST request. Credit dto: " + creditResponse);

        Credit credit = creditService.createCredit(creditResponse.getBody());
        log.info("Credit: " + credit);

        creditService.addCreditToDB(credit);
        log.info("Credit add!");

        application.setCreditId(credit.getId());
        applicationService.addApplicationToDB(application);

        messageService.sendMessage(ApplicationStatus.CC_APPROVED, client.getEmail(), applicationId, TOPIC_CREATE_DOCUMENT);
        } catch (Exception e) {
            messageService.sendMessage(ApplicationStatus.CC_DENIED, client.getEmail(), applicationId, TOPIC_APPLICATION_DENIED);
            throw  new IllegalArgumentException("Scoring failed");
        }
    }

    @Operation(
            summary = "request to send documents",
            description = "request to send documents + sending a message to the mail"
    )
    @PostMapping("/document/{applicationId}/send")
    public void requestToSendDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        try {
            dealService.sendMessage(applicationId, TOPIC_SEND_DOCUMENTS, ApplicationStatus.PREPARE_DOCUMENTS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Operation(
            summary = "request to sign documents",
            description = "approval/client's refusal + request to sign documents. sending a message to the mail"
    )
    @PostMapping("/document/{applicationId}/sign")
    public void requestToSignDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        try {
            //тут какое-то условие, чтобы клиент отказался?
            if(true) {
                //какое-то формирование ПЭП???
                dealService.sendMessage(applicationId, TOPIC_SEND_SES, ApplicationStatus.DOCUMENT_SIGNED);
            } else {
                dealService.sendMessage(applicationId, TOPIC_APPLICATION_DENIED, ApplicationStatus.CLIENT_DENIED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Operation(
            summary = "signing of documents",
            description = "signing of documents + sending a message to the mail"
    )
    @PostMapping("/document/{applicationId}/code")
    public void signingOfDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        try {
            dealService.sendMessage(applicationId, TOPIC_CREDIT_ISSUED, ApplicationStatus.CREDIT_ISSUED);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Operation(
            summary = "get applicationDTO",
            description = "getting information about the application by application id"
    )
    @GetMapping("/get/application/{applicationId}")
    public ResponseEntity<LoanApplicationRequestDTO> getApplicationById(@PathVariable(value = "applicationId") Long applicationId) {
        return dealService.getLoanApplicationByApplicationId(applicationId);
    }

    @Operation(
            summary = "get creditDTO",
            description = "getting information about the credit by application id"
    )
    @GetMapping("/get/credit/{applicationId}")
    public ResponseEntity<CreditDTO> getCreditByApplicationId(@PathVariable(value = "applicationId") Long applicationId) {
        return dealService.getCreditByApplicationId(applicationId);
    }

    @Operation(
            summary = "get offedDTO",
            description = "getting information about the offer by application id"
    )
    @GetMapping("/get/offer/{applicationId}")
    public ResponseEntity<LoanOfferDTO> getOfferByApplicationId(@PathVariable(value = "applicationId") Long applicationId) {
        return dealService.getOfferByApplicationId(applicationId);
    }
}
