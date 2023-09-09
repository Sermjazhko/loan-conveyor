package com.dossier.integration;

import com.dossier.dto.CreditDTO;
import com.dossier.dto.LoanApplicationRequestDTO;
import com.dossier.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private static final String URL_DEAL_GET_APPLICATION = "/deal/get/application/";
    private static final String URL_DEAL_GET_CREDIT = "/deal/get/credit/";
    private static final String URL_DEAL_GET_OFFER = "/deal/get/offer/";

    @Value("${integration.deal.url}")
    private String urlDeal;

    private final RestTemplate restTemplate;

    public LoanApplicationRequestDTO getApplicationById(Long id) {
        log.info("URL: " + urlDeal + URL_DEAL_GET_APPLICATION + id);

        LoanApplicationRequestDTO application = restTemplate.getForObject(urlDeal + URL_DEAL_GET_APPLICATION + id, LoanApplicationRequestDTO.class);

        log.info("application: " + application);
        return application;
    }

    public CreditDTO getCreditByApplicationId(Long id) {
        log.info("URL: " + urlDeal + URL_DEAL_GET_CREDIT + id);

        CreditDTO creditDTO = restTemplate.getForObject(urlDeal + URL_DEAL_GET_CREDIT + id, CreditDTO.class);

        log.info("credit: " + creditDTO);
        return creditDTO;
    }


    public LoanOfferDTO getOfferByApplicationId(Long id) {
        log.info("URL: " + urlDeal + URL_DEAL_GET_OFFER + id);

        LoanOfferDTO loanOfferDTO = restTemplate.getForObject(urlDeal + URL_DEAL_GET_OFFER + id, LoanOfferDTO.class);

        log.info("offer: " + loanOfferDTO);
        return loanOfferDTO;
    }
}
