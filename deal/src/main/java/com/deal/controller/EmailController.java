package com.deal.controller;

import com.deal.enums.ApplicationStatus;
import com.deal.model.Application;
import com.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "Email controller", description = "Responsible for communication between microservices")
@RestController
@RequestMapping("/deal/document")
@RequiredArgsConstructor
public class EmailController {

    private static final String TOPIC_SEND_DOCUMENTS = "send-documents";
    private static final String TOPIC_SEND_SES = "send-ses";
    private static final String TOPIC_CREDIT_ISSUED = "credit-issued";
    private static final String TOPIC_APPLICATION_DENIED = "application-denied";

    private final DealService dealService;

    @Operation(
            summary = "request to send documents",
            description = "request to send documents + sending a message to the mail"
    )
    @PostMapping("/{applicationId}/send")
    public void requestToSendDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        try {
            dealService.updateApplication(dealService.getApplicationById(applicationId), TOPIC_SEND_DOCUMENTS, ApplicationStatus.PREPARE_DOCUMENTS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Operation(
            summary = "request to sign documents",
            description = "approval/client's refusal + request to sign documents. sending a message to the mail"
    )
    @PostMapping("/{applicationId}/sign")
    public void requestToSignDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        try {
            Application application = dealService.getApplicationById(applicationId);
            application = dealService.setSesCode(application);
            dealService.updateApplication(application, TOPIC_SEND_SES, ApplicationStatus.DOCUMENT_SIGNED);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Operation(
            summary = "signing of documents",
            description = "signing of documents + sending a message to the mail"
    )
    @PostMapping("/{applicationId}/code")
    public void signingOfDocuments(@PathVariable(value = "applicationId") Long applicationId, @RequestParam String ses) {
        try {
            if (dealService.checkSesCode(ses, applicationId)) {
                Application application = dealService.getApplicationById(applicationId);
                application = dealService.setSignDate(application);
                dealService.updateApplication(application, TOPIC_CREDIT_ISSUED, ApplicationStatus.CREDIT_ISSUED);
            } else {
                dealService.updateApplication(dealService.getApplicationById(applicationId), TOPIC_APPLICATION_DENIED, ApplicationStatus.CLIENT_DENIED);
                log.error("Incorrect ses code");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
