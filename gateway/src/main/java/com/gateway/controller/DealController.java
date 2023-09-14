package com.gateway.controller;

import com.gateway.dto.FinishRegistrationRequestDTO;
import com.gateway.integration.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "Deal controller", description = "Integration of the deal microservice")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @Operation(
            summary = "Completion of registration + full credit calculation.",
            description = "The application is retrieved from the database by applicationId." +
                    "The ScoringDataDTO is saturated with information from the FinishRegistrationRequestDTO " +
                    "and the Client, which is stored in the Application" +
                    "A POST request is sent to the MC CC with the body of the ScoringDataDTO." +
                    "The loan body is created and added to the database."
    )
    @PutMapping("/deal/calculate/{applicationId}")
    public void calculationLoanParameters(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                          @PathVariable(value = "applicationId") Long applicationId) {
        log.info("Input data to the calculation, FinishRegistrationRequestDTO: " + finishRegistrationRequestDTO +
                ", application id" + applicationId);
        dealService.calculationLoanParameters(finishRegistrationRequestDTO, applicationId);
    }

    @Operation(
            summary = "request to send documents",
            description = "request to send documents + sending a message to the mail"
    )
    @PostMapping("/deal/document/{applicationId}/send")
    public void requestToSendDocuments(@PathVariable(value = "applicationId") Long applicationId) {

        log.info("Input data, applicationId: " + applicationId);
        dealService.completeRegistration(applicationId);
    }

    @Operation(
            summary = "request to sign documents",
            description = "approval/client's refusal + request to sign documents. sending a message to the mail"
    )
    @PostMapping("/deal/document/{applicationId}/sign")
    public void requestToSignDocuments(@PathVariable(value = "applicationId") Long applicationId) {

        log.info("Input data, applicationId: " + applicationId);
        dealService.requestToSignDocuments(applicationId);
    }

    @Operation(
            summary = "signing of documents",
            description = "signing of documents + sending a message to the mail"
    )
    @PostMapping("/deal/document/{applicationId}/code")
    public void requestToSendDocuments(@PathVariable(value = "applicationId") Long applicationId, @RequestParam String ses) {

        log.info("Input data, applicationId: " + applicationId + ", ses: " + ses);
        dealService.signingOfDocuments(ses, applicationId);
    }

}
