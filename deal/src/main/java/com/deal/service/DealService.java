package com.deal.service;

import com.deal.enums.ApplicationStatus;
import com.deal.model.Application;
import com.deal.model.Client;
import com.deal.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

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

    private String generationSesCode() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    public Application getApplicationById(Long applicationId) {
        return applicationService.getApplicationById(applicationId);
    }

    public ResponseEntity<List<Application>> getApplications() {
        return new ResponseEntity<>(applicationService.getApplications(), HttpStatus.OK);
    }

    public void updateApplication(Application application, String topicName, ApplicationStatus applicationStatus) {
        log.info("status: " + applicationStatus + ", application: " + application + ", topic: " + topicName);
        applicationService.addApplicationToDB(application, new Date(), applicationStatus);
        sendMessage(application.getId(), topicName, applicationStatus);
        log.info("application: " + application);
    }

    public Boolean checkSesCode(String ses, Long applicationId) {
        Application application = applicationService.getApplicationById(applicationId);
        String applicationSes = application.getSesCode();
        log.info("ses: " + ses + ", correct ses: " + applicationSes);
        return ses.equals(applicationSes);
    }

    public Application setSesCode(Application application) {
        application.setSesCode(generationSesCode());
        return application;
    }

    public Application setSignDate(Application application) {
        application.setSignDate(new Date());
        return application;
    }
}
