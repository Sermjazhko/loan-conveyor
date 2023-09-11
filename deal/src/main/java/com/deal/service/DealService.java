package com.deal.service;

import com.deal.enums.ApplicationStatus;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private final ApplicationService applicationService;
    private final MessageService messageService;

    public ResponseEntity<Application> getLoanApplicationByApplicationId(Long applicationId) {
        Application application = applicationService.getApplicationById(applicationId);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    private void sendMessage(Long applicationId, String topicName, ApplicationStatus applicationStatus) {
        log.info("status: " + applicationStatus + ", id: " + applicationId + ", topic: " + topicName);
        Application application = applicationService.getApplicationById(applicationId);
        Client client = application.getClient();
        log.info("client's email address: " + client.getEmail());
        messageService.sendMessage(applicationStatus, client.getEmail(), applicationId, topicName);
    }

    public void updateApplication(Long applicationId, String topicName, ApplicationStatus applicationStatus) {
        log.info("status: " + applicationStatus + ", id: " + applicationId + ", topic: " + topicName);
        Application application = applicationService.getApplicationById(applicationId);
        applicationService.addApplicationToDB(application, new Date(), applicationStatus);
        sendMessage(applicationId, topicName, applicationStatus);
        log.info("application: " + application);
    }

    public Boolean checkSesCode(String ses, Long applicationId) {
        Application application = applicationService.getApplicationById(applicationId);
        String applicationSes = application.getSesCode();
        log.info("ses: " + ses + ", correct ses: " + applicationSes);
        return ses.equals(applicationSes);
    }
}
