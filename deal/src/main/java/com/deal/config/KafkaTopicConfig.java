package com.deal.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic finishRegistration() {
        return TopicBuilder.name("finish-registration").build();
    }
    @Bean
    public NewTopic createDocuments() {
        return TopicBuilder.name("create-documents").build();
    }
    @Bean
    public NewTopic sendDocuments() {
        return TopicBuilder.name("send-documents").build();
    }
    @Bean
    public NewTopic sendSes() {
        return TopicBuilder.name("send-ses").build();
    }
    @Bean
    public NewTopic creditIssued() {
        return TopicBuilder.name("credit-issued").build();
    }
    @Bean
    public NewTopic applicationDenied() {
        return TopicBuilder.name("application-denied").build();
    }
}
