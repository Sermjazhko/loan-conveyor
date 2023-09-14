package com.gateway.integration;

import com.gateway.dto.FinishRegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private static final String URL_DEAL = "/deal";
    private static final String URL_DOCUMENT = "/document";

    @Value("${integration.deal.url}")
    private String urlDeal;

    private final RestTemplate restTemplate;

    public void calculationLoanParameters(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        log.info("URL: " + urlDeal + URL_DEAL + "/calculate/" +  applicationId);

        restTemplate.put(urlDeal + URL_DEAL + "/calculate/" +  applicationId, finishRegistrationRequestDTO);
        log.info("calculation loan parameters");
    }

    public void completeRegistration(Long applicationId) {
        log.info("URL: " + urlDeal + URL_DEAL + URL_DOCUMENT+ "/" +  applicationId + "/send");


        restTemplate.postForObject(urlDeal + URL_DEAL + URL_DOCUMENT+ "/" +  applicationId + "/send", applicationId, Long.class);
        log.info("complete registration");
    }

    public void requestToSignDocuments(Long applicationId) {
        log.info("URL: " + urlDeal + URL_DEAL + URL_DOCUMENT+ "/" +  applicationId + "/sign");

        restTemplate.postForObject(urlDeal + URL_DEAL + URL_DOCUMENT+ "/" +  applicationId + "/sign", applicationId, Long.class);
        log.info("request to sign documents");
    }

    public void signingOfDocuments(String ses, Long applicationId) {

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("applicationId", applicationId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlDeal + URL_DEAL + URL_DOCUMENT+ "/{applicationId}/code")
                .queryParam("ses", ses);

        log.info("url: " + builder.buildAndExpand(urlParams).toUri());

        restTemplate.postForObject(builder.buildAndExpand(urlParams).toUri(), ses, String.class);

        log.info("signing of documents");
    }


}
