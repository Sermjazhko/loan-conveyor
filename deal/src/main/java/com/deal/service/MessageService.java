package com.deal.service;

import com.deal.dto.EmailMessage;
import com.deal.enums.ApplicationStatus;
import com.deal.producer.KafkaSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    private final ObjectMapper objectMapper;
    private final KafkaSender kafkaSender;

    public MessageService(ObjectMapper objectMapper, KafkaSender kafkaSender) {
        this.objectMapper = objectMapper;
        this.kafkaSender = kafkaSender;
    }

    public void sendMessage(ApplicationStatus applicationStatus, String address, Long applicationId, String topicName) {
        try {
            log.info("status: " + applicationStatus + ", recipient's address: " + address + ", id: " + applicationId + ", topic: " + topicName);
            EmailMessage emailMessage = EmailMessage.builder()
                    .theme(applicationStatus)
                    .address(address)
                    .applicationId(applicationId)
                    .build();
            String jsonStr = objectMapper.writeValueAsString(emailMessage);
            kafkaSender.sendMessage(jsonStr, topicName);
            log.info("deal has successfully sent the message");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
