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
            description = "The application is retrieved from the database by applicationId."
    )
    @PostMapping ("/application/registration/{applicationId}")
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
    @PostMapping("/document/{applicationId}")
    public void requestToSendDocuments(@PathVariable(value = "applicationId") Long applicationId) {

        log.info("Input data, applicationId: " + applicationId);
        dealService.completeRegistration(applicationId);
    }

    @Operation(
            summary = "request to sign documents",
            description = "approval/client's refusal + request to sign documents. sending a message to the mail"
    )
    @PostMapping("/document/{applicationId}/sign")
    public void requestToSignDocuments(@PathVariable(value = "applicationId") Long applicationId) {

        log.info("Input data, applicationId: " + applicationId);
        dealService.requestToSignDocuments(applicationId);
    }

    @Operation(
            summary = "signing of documents",
            description = "signing of documents + sending a message to the mail"
    )
    @PostMapping("/document/{applicationId}/sign/code")
    public void requestToSendDocuments(@PathVariable(value = "applicationId") Long applicationId, @RequestParam String ses) {

        log.info("Input data, applicationId: " + applicationId + ", ses: " + ses);
        dealService.signingOfDocuments(ses, applicationId);
    }
}
