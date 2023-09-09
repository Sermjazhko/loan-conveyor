package com.dossier.service;

import com.dossier.dto.*;
import com.dossier.integration.DealService;
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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DossierService {
    private static final String PATH_FILES = "src\\main\\resources\\files\\";
    private static final String PROFILE = "profile.txt";
    private static final String PAYMENT_SCHEDULE = "paymentSchedule.txt";
    private static final String LOAN_AGREEMENT = "loanAgreement.txt";
    private static final String URL_DEAL_POST_DOCUMENT = "/deal/document/";
    private static final String URL_DEAL_POST_CALCULATE = "/deal/calculate/";
    private static final String CODE = "/code";
    private static final String SIGN = "/sign";
    private static final String SEND = "/send";

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

        //ссылка для постмана (просто для навигации)
        String text = "Вы выбрали кредитное предложение, завершите регистрацию: " +
                urlDeal + URL_DEAL_POST_CALCULATE + applicationId;
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
        log.info("");
    }

    public void createDocument(String message) throws JsonProcessingException {
        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);

        Long applicationId = emailMessage1.getApplicationId();
        //ссылка для постмана (просто для навигации)
        String text = "Отправьте запрос на создание документов: " +
                urlDeal + URL_DEAL_POST_DOCUMENT + applicationId + SEND;
        sendSimpleMessage(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);

    }

    public void sendDocuments(String message) throws JsonProcessingException {

        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);
        Long applicationId = emailMessage1.getApplicationId();
        LoanApplicationRequestDTO loanApplicationRequestDTO = getApplicationById(applicationId);
        LoanOfferDTO loanOfferDTO = getOfferByApplicationId(applicationId);
        CreditDTO creditDTO = getCreditByApplicationId(applicationId);

        createProfile(loanApplicationRequestDTO, loanOfferDTO);
        createPaymentSchedule(creditDTO);
        createLoanAgreement(loanApplicationRequestDTO, creditDTO);

        //ссылка для постмана (просто для навигации)
        String text = "Запрос на подписание документов: " + urlDeal + URL_DEAL_POST_DOCUMENT + applicationId + SIGN;
        sendMessageWithAttachment(emailMessage1.getAddress(), emailMessage1.getTheme().toString(), text);
    }

    public void sendSes(String message) throws JsonProcessingException {

        EmailMessage emailMessage1 = objectMapper.readValue(message, EmailMessage.class);
        Long applicationId = emailMessage1.getApplicationId();

        //ссылка для постмана (просто для навигации)
        String text = "тут должна быть ПЭП. Запрос на подписание документов: " + urlDeal + URL_DEAL_POST_DOCUMENT + applicationId + CODE;
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

    private void createProfile(LoanApplicationRequestDTO loanApplicationRequestDTO, LoanOfferDTO loanOfferDTO) {

        Path path = Paths.get(PATH_FILES + PROFILE);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            bufferedWriter.write("Profile: \n\n");
            bufferedWriter.write("First Name: " + loanApplicationRequestDTO.getFirstName() + "\n");
            bufferedWriter.write("Last Name: " + loanApplicationRequestDTO.getLastName() + "\n");
            bufferedWriter.write("Middle Name: " + loanApplicationRequestDTO.getMiddleName() + "\n");
            bufferedWriter.write("Email: " + loanApplicationRequestDTO.getEmail() + "\n");
            bufferedWriter.write("Birthdate: " + loanApplicationRequestDTO.getBirthdate() + "\n");
            bufferedWriter.write("Passport series: " + loanApplicationRequestDTO.getPassportSeries() + "\n");
            bufferedWriter.write("Passport number: " + loanApplicationRequestDTO.getPassportNumber() + "\n");
            bufferedWriter.write("Loan offer: \n");
            bufferedWriter.write("Request amount: " + loanOfferDTO.getRequestedAmount() + "\n");
            bufferedWriter.write("Term: " + loanOfferDTO.getTerm() + "\n");
            bufferedWriter.write("Rate: " + loanOfferDTO.getRate() + "\n");
            bufferedWriter.write("Monthly payment: " + loanOfferDTO.getMonthlyPayment() + "\n");
            bufferedWriter.close();
            log.info("profile (file) created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createPaymentSchedule(CreditDTO creditDTO) {
        Path path = Paths.get(PATH_FILES + PAYMENT_SCHEDULE);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            bufferedWriter.write("Payment schedule: \n\n");
            List<PaymentScheduleElement> list = creditDTO.getPaymentSchedule();
            for (int i = 0; i < list.size(); ++i) {
                bufferedWriter.write(creditDTO.getPaymentSchedule().get(i) + "\n");
            }
            bufferedWriter.close();
            log.info("payment schedule (file) created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createLoanAgreement(LoanApplicationRequestDTO loanApplicationRequestDTO, CreditDTO creditDTO) {
        Path path = Paths.get(PATH_FILES + LOAN_AGREEMENT);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
            bufferedWriter.write("Loan agreement: \n\n");
            bufferedWriter.write("Data: \n\n");
            bufferedWriter.write("First Name: " + loanApplicationRequestDTO.getFirstName() + "\n");
            bufferedWriter.write("Last Name: " + loanApplicationRequestDTO.getLastName() + "\n");
            bufferedWriter.write("Middle Name: " + loanApplicationRequestDTO.getMiddleName() + "\n");
            bufferedWriter.write("Email: " + loanApplicationRequestDTO.getEmail() + "\n");
            bufferedWriter.write("Birthdate: " + loanApplicationRequestDTO.getBirthdate() + "\n");
            bufferedWriter.write("Passport series: " + loanApplicationRequestDTO.getPassportSeries() + "\n");
            bufferedWriter.write("Passport number: " + loanApplicationRequestDTO.getPassportNumber() + "\n");
            bufferedWriter.write("Loan details: \n");
            bufferedWriter.write("Amount: " + creditDTO.getAmount() + "\n");
            bufferedWriter.write("Term: " + creditDTO.getTerm() + "\n");
            bufferedWriter.write("Rate: " + creditDTO.getRate() + "\n");
            bufferedWriter.write("Monthly payment: " + creditDTO.getMonthlyPayment() + "\n");
            bufferedWriter.write("PSK: " + creditDTO.getPsk() + "\n\n");

            bufferedWriter.write("Payment schedule: \n\n");

            List<PaymentScheduleElement> list = creditDTO.getPaymentSchedule();
            for (int i = 0; i < list.size(); ++i) {
                bufferedWriter.write(creditDTO.getPaymentSchedule().get(i) + "\n");
            }
            bufferedWriter.close();
            log.info("loan agreement (file) created");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    private LoanApplicationRequestDTO getApplicationById(Long id) {
        return dealService.getApplicationById(id);
    }

    private CreditDTO getCreditByApplicationId(Long id) {
        return dealService.getCreditByApplicationId(id);
    }

    private LoanOfferDTO getOfferByApplicationId(Long id) {
        return dealService.getOfferByApplicationId(id);
    }
}
