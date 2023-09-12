package com.dossier.consumer;

import com.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final DossierService dossierService;

    @KafkaListener(topics = "finish-registration",
            groupId = "${spring.kafka.consumer.group-id}")
    void listenerFinishRegistration(String message) {
        try {
            dossierService.finishRegistration(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(
            topics = "create-documents",
            groupId = "${spring.kafka.consumer.group-id}")
    void listenerCreateDocument(String message) {
        try {
            dossierService.createDocument(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(
            topics = "send-documents",
            groupId = "${spring.kafka.consumer.group-id}")
    void listenerSendDocuments(String message){
        try {
            dossierService.sendDocuments(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(
            topics = "send-ses",
            groupId = "${spring.kafka.consumer.group-id}")
    void listenerSendSes(String message) {
        try {
            dossierService.sendSes(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(
            topics = "credit-issued",
            groupId = "${spring.kafka.consumer.group-id}")
    void listenerCreditIssued(String message) {
        try {
            dossierService.creditConfirmation(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @KafkaListener(
            topics = "application-denied",
            groupId = "${spring.kafka.consumer.group-id}")
    void listenerApplicationDenied(String message) {
        try {
            dossierService.rejectionOfApplication(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
