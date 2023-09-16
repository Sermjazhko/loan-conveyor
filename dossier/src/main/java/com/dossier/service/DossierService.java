package com.dossier.service;

import com.dossier.dto.*;
import com.dossier.integration.DealService;
import com.dossier.model.Application;
import com.dossier.model.Client;
import com.dossier.model.Credit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Component
@RequiredArgsConstructor
public class DossierService {
    private static final String PATH_FILES = "dossier\\src\\main\\resources\\files\\";
    private static final String PROFILE = "profile.txt";
    private static final String PAYMENT_SCHEDULE = "paymentSchedule.txt";
    private static final String LOAN_AGREEMENT = "loanAgreement.txt";
    private static final String CODE = "/code";
    private static final String SIGN = "/sign";

    @Value("${integration.deal.url}")
    private String urlDeal;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private JavaMailSender emailSender;

    private final DealService dealService;

    private final ObjectMapper objectMapper;

    public void finishRegistration(String message) throws JsonProcessingException {
        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);

        Long applicationId = emailMessage1.getApplicationId();

        String text = "Вы выбрали кредитное предложение, завершите регистрацию: " +
                "http://localhost:9085/application/registration/" + applicationId;
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
        log.info("");
    }

    public void createDocument(String message) throws JsonProcessingException {
        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);

        Long applicationId = emailMessage1.getApplicationId();
        //ссылка для постмана (просто для навигации)
        String text = "Отправьте запрос на создание документов: " +
                "http://localhost:9085/document/" + applicationId;
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);

    }

    public void sendDocuments(String message) throws JsonProcessingException {

        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);
        Long applicationId = emailMessage1.getApplicationId();
        Application application = getApplicationById(applicationId);
        Client client = getOfferByApplicationId(applicationId);
        Credit credit = getCreditByApplicationId(applicationId);

        createProfile(application, client);
        createPaymentSchedule(credit);
        createLoanAgreement(application, client, credit);

        String text = "Запрос на подписание документов: " + "http://localhost:9085/document/" + applicationId + SIGN;
        sendMessageWithAttachment(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
    }

    public void sendSes(String message) throws JsonProcessingException {

        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);
        Long applicationId = emailMessage1.getApplicationId();
        Application application = getApplicationById(applicationId);
        String text = "ПЭП: " + application.getSesCode() + ". Запрос на подписание документов: " +
                "http://localhost:9085/document/" + applicationId + SIGN + CODE;
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
    }

    public void creditConfirmation(String message) throws JsonProcessingException {

        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);
        String text = "Вы успешно взяли кредит!";
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
    }

    public void rejectionOfApplication(String message) throws JsonProcessingException {

        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);
        String text = "Заявка отклонена";
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
    }


    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setFrom(username);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            log.info("the message was sent to: " + to);
        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
        }
    }

    private void sendMessageWithAttachment(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();

        String pathToProfile = PATH_FILES + PROFILE;
        String pathToPaymentSchedule = PATH_FILES + PAYMENT_SCHEDULE;
        String pathToLoanAgreement = PATH_FILES + LOAN_AGREEMENT;

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource fileProfile
                    = new FileSystemResource(new File(pathToProfile));
            helper.addAttachment(PROFILE, fileProfile);
            FileSystemResource filePayment
                    = new FileSystemResource(new File(pathToPaymentSchedule));
            helper.addAttachment(PAYMENT_SCHEDULE, filePayment);
            FileSystemResource fileLoan
                    = new FileSystemResource(new File(pathToLoanAgreement));
            helper.addAttachment(LOAN_AGREEMENT, fileLoan);

            emailSender.send(message);
            log.info("message with the documents was sent to: " + to);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
    }

    private void createProfile(Application application, Client client) {

        Path path = Paths.get(PATH_FILES + PROFILE);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            bufferedWriter.write("Profile: \n\n");
            bufferedWriter.write("First Name: " + client.getFirstName() + "\n");
            bufferedWriter.write("Last Name: " + client.getLastName() + "\n");
            bufferedWriter.write("Middle Name: " + client.getMiddleName() + "\n");
            bufferedWriter.write("Email: " + client.getEmail() + "\n");
            bufferedWriter.write("Birthdate: " + client.getBirthday() + "\n");
            bufferedWriter.write("Passport: " + client.getPassport() + "\n");

            bufferedWriter.write("Loan offer: \n");
            bufferedWriter.write("Request amount: " + application.getAppliedOffer() + "\n");
            bufferedWriter.close();
            log.info("profile (file) created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createPaymentSchedule(Credit credit) {
        Path path = Paths.get(PATH_FILES + PAYMENT_SCHEDULE);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            bufferedWriter.write("Payment schedule: \n\n");

            bufferedWriter.write(credit.getPaymentSchedule());
            bufferedWriter.close();
            log.info("payment schedule (file) created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createLoanAgreement(Application application, Client client, Credit credit) {
        Path path = Paths.get(PATH_FILES + LOAN_AGREEMENT);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            bufferedWriter.write("Loan agreement: \n\n");
            bufferedWriter.write("Data: \n\n");
            bufferedWriter.write("First Name: " + client.getFirstName() + "\n");
            bufferedWriter.write("Last Name: " + client.getLastName() + "\n");
            bufferedWriter.write("Middle Name: " + client.getMiddleName() + "\n");
            bufferedWriter.write("Email: " + client.getEmail() + "\n");
            bufferedWriter.write("Birthdate: " + client.getBirthday() + "\n");
            bufferedWriter.write("Passport: " + client.getPassport() + "\n");

            bufferedWriter.write("Loan details: \n");
            bufferedWriter.write("Request amount: " + application.getAppliedOffer() + "\n");
            bufferedWriter.write("Payment schedule: \n\n");
            bufferedWriter.write(credit.getPaymentSchedule());
            bufferedWriter.close();
            log.info("loan agreement (file) created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Application getApplicationById(Long id) {
        return dealService.getApplicationById(id);
    }

    private Credit getCreditByApplicationId(Long id) {
        return dealService.getApplicationById(id).getCredit();
    }

    private Client getOfferByApplicationId(Long id) {
        return dealService.getApplicationById(id).getClient();
    }
}
